import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

@WebServlet("/consulta")
public class Consulta extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	DataSource pool;
	@Override
	public void init() throws ServletException {
		
		super.init();
		
		try {
			InitialContext contexto = new InitialContext();
			
			pool = (DataSource) contexto.lookup("java:comp/env/jdbc/mysql_tiendalibros");
			
			if (pool == null) {
				throw new ServletException("Error al acceder al POOL de conexiones: POOL NULL");
			}
			
		} catch (NamingException e) {
			System.out.println("Error al acceder al POOL de conexiones catch");
			e.printStackTrace();
		}
	}

	public Consulta() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// Recogemos los datos del formulario.
		String autor = request.getParameter("autor");

		// Preparamos la salida del servlet.
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		// Creamos los objetos de conexión a la base de datos.
		Connection con = null;
		PreparedStatement stmt = null;

		try {

			// Cargamos el driver JDBC
			Class.forName("com.mysql.jdbc.Driver");

			// Conectamos con la base de datos.
//			String url = "jdbc:mysql://localhost/TiendaLibros";
//			String usuario = "librero";
//			String contrasenya = "Ageofempires2";

			// Ejecutamos una consulta SQL
			con = pool.getConnection();

			String sql = "select * from libros where autor=?";
			stmt = con.prepareStatement(sql);
			stmt.setString(1, autor);

			ResultSet resultados = stmt.executeQuery();

			// Mostramos los resultados
			out.println("<html>");
			out.println("<head>");
			out.println("<meta charset='utf-8'>");
			out.println("<title>Consulta</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<h1>Consulta por autor</h1>");
			out.println("<p>Los libros publicados por " + autor + "son:</p>");

			while (resultados.next()) {

				out.println("<hr>");
				out.println("Título: " + resultados.getString("titulo")
						+ "<br>");
				out.println("Precio: " + resultados.getString("precio")
						+ "<br>");
				out.println("Cantidad: " + resultados.getString("cantidad")
						+ "<br>");

				out.println("<hr>");
			}

			out.println("</body>");
			out.println("</html>");

		} catch (Exception e) {
			System.out.println("ERROR EN LA CONEXIÓN A LA BASE DE DATOS");
			e.printStackTrace();
		}finally{
			try {
				con.close();
			} catch (SQLException e) {
				System.out.println("ERROR AL CERRAR A LA BASE DE DATOS");
				e.printStackTrace();
			}
		}

	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

	}

}
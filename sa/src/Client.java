import java.sql.SQLException;

import courseContentManagement.Course;
import courseManagement.CourseManagement;
import graphics.Login;
import user.User;

public class Client {
	public static void main(String[] args)
	 {
		try {
			Login l = new Login();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	 }
}

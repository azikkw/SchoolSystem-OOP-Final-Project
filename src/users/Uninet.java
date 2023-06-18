package users;

import java.io.IOException;
import attributes.DataBase;

public class Uninet 
{
	public static void main(String[] args) throws IOException, ClassNotFoundException 
	{
		DataBase.DeserializeAll();
		UserController.logIn();
	}
}

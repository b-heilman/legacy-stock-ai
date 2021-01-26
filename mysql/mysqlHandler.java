package mysql;

import java.sql.DriverManager;
import java.util.List;
import com.mysql.jdbc.*;
/*-------------------
 * To get this class running you need to make sure that the my-sql connector
 * is placed in ('java system library'\jre\lib\ext), and then modify mySQL to 
 * run on: (mysql\bin\my.cnf) collation-server = latin1_swedish_ci
 *
  CREATE USER 'stocker'@'%' IDENTIFIED BY 'st0cks';
  GRANT SELECT , INSERT , UPDATE , DELETE , CREATE , DROP , 
  FILE , INDEX , ALTER , CREATE TEMPORARY TABLES , CREATE VIEW ,
  SHOW VIEW , CREATE ROUTINE, ALTER ROUTINE, EXECUTE ON * . * TO 'stocker'@'%' 
  WITH MAX_QUERIES_PER_HOUR 0 MAX_CONNECTIONS_PER_HOUR 0 MAX_UPDATES_PER_HOUR 0 MAX_USER_CONNECTIONS 0 ;
 * 
 */

public class mysqlHandler {
	private static final String SERVER = "jdbc:mysql://xavier:3306/";
	private static final String USERNAME = "stocker";
	private static final String PASSWORD = "st0cks";
	private Connection con;
	
	public mysqlHandler(String db, String server, String user, String pass){
		creator(db, server, user, pass);
		}
	
	public mysqlHandler(String db){
		creator(db, SERVER, USERNAME, PASSWORD);
		}
	
	private void creator(String db, String server, String user, String pass){
		try{
			Class.forName("com.mysql.jdbc.Driver");
			
			con = (Connection)DriverManager.getConnection(server+db,user, pass);
			// if this bombs that means the db hasn't been created yet and needs
			// to be, so catch the exception and setup the database.
			}
		catch( Exception e){
			try{
				setupDatabase(db, server, user, pass);
			
				con = (Connection)DriverManager.getConnection(server+db,user, pass);
				}
			catch(Exception ex){
				ex.printStackTrace();
				}
			}//end catch
		}
	
	private void setupDatabase(String db, String server, String user, String pass) 
		throws Exception{
			Class.forName("com.mysql.jdbc.Driver");
			
			Connection local = 
				 	(Connection)DriverManager.getConnection(server+"mysql",user, pass);
			
			Statement stmt = (Statement)local.createStatement();
			
			stmt.executeUpdate("CREATE DATABASE "+db);
		}
	/*----------
	 * generic access functions 
	 */
	public ResultSet query(String q){
		try{
			Statement stmt = (Statement)con.createStatement();
     
			return (ResultSet)stmt.executeQuery (q);
			} 
		catch( Exception e ){
			System.out.println("*QUERY* --> "+q);
			e.printStackTrace();
			System.exit(2);
			return (ResultSet) null;
			} 	
		}
	
	public long structure(String q){
		try{
			Statement stmt = (Statement)con.createStatement();
			return stmt.executeUpdate(q);
			} 
		catch( Exception e ){
			System.out.println("*QUERY* --> "+q);
			e.printStackTrace();
			System.exit(3);
			return -1;
			} 		
		}
	/*----------
	 * These are the functions designed to make mySQL easier to use
	 */
	// generate a table (null/not null)
	public void createTable(String table, List<mysqlToken> in, boolean auto){
		String qString = "CREATE TABLE `"+table+"` (";
		if (auto)
			qString += "`id` INT unsigned not null auto_increment, ";
		
		for (int i = 0; i < in.size(); i++)
			{
			qString += "`"+in.get(i).columnName()+"` ";
			
			if (in.get(i).type() == null)
				qString += "TEXT ";
			else
				qString += in.get(i).type()+" ";
			
			if (in.get(i).canBeNull())
				qString += "NULL ";
			else
				qString += "NOT NULL ";
			
			if (in.get(i).value() != null)
				qString += "DEFAULT "+in.get(i).value();
			
			qString += ", ";
			}
		if (auto)
			qString += "PRIMARY KEY ( `id` )";
		else
			qString += "PRIMARY KEY ( `"+in.get(0).columnName()+"` )";
			
		qString +=");";
		
		structure(qString);
		}
	// table exists
	public boolean tableExists(String table){
		try{
			String qString = "SHOW TABLES LIKE '"+table+"'";
		
			ResultSet res = query(qString);
		
			return res.first();
			}
		catch (Exception ex){
			return false;	
			}
		}
	// table has data
	public boolean tableHasData(String table){
		try{
			String qString = "Select * From `"+table+"` LIMIT 0 , 1";
			
			ResultSet res = query(qString);
			
			return res.first();
			}
		catch (Exception ex){
			return false;	
			}
		}
	
	// all data from table
	public ResultSet tableDump(String table){
		String qString = "Select * From `"+table+"`";
		
		return query(qString);
		}
	
	public ResultSet tableDumpRange(String table, int bottom, int top){
		String qString = "Select * From `"+table+"` LIMIT "+bottom+", "+top;
		
		return query(qString);
		}
	// True puts the highest value at the top, false the lowest
	public ResultSet tableDump(String table, String field, boolean how){
		String qString = "Select * From `"+table+"` ";
		qString += "ORDER BY `"+field+"` ";
		
		if (how)
			qString += "DESC";
		else
			qString += "ASC";
		
		return query(qString);
		}
	// quick little helper function
	private String replace(String in, String what, String with){
		return in.replaceAll(what,with);
		}
	// table insert (returns id)
	public long tableInsert(List<mysqlToken> in, String table){
		String fields = "";
		String values = "";
		
		for (int i = 0; i < in.size(); i++)
			{
			if (i > 0)
				{
				fields += ",";
				values += ",";
				}
			fields += "`"+in.get(i).columnName()+"`";
			values +=	"'"+replace(in.get(i).value(),"'","\'")+"'";
			}
		
		String qString = "INSERT INTO `"+table+"` ("+fields+") VALUES ("+values+");";
		
		return structure(qString);
		}
	//In thought, this .type() could prolly be changed to .compare() to get the
	// the class's comparator e.g. INT gets = , VARCHAR get LIKE
	public long tableUpdate(List<mysqlToken> where, List<mysqlToken> in, String table){
		String qString = "UPDATE `"+table+"` SET ";
		
		for (int i = 0; i < in.size(); i++)
			{
			if (i > 0)
				{
				qString += ",";
				}
			qString += " `"+in.get(i).columnName()+"` = '"
											+replace(in.get(i).value(),"'","\'")+"' ";
			}
		qString +=  "WHERE ";
		
		for (int i = 0; i < where.size(); i++)
			{
			if (i > 0)
				{
				qString += "AND";
				}
				
			qString += " CONVERT( `"+where.get(i).columnName()+"` USING utf8 ) "+where.get(i).search()+" '"
									+replace(where.get(i).value(),"'","\'")+"' ";
			}
		qString += "LIMIT 1;";
		
		return structure(qString);
		}
	
	public ResultSet tableQuickSearch(List<mysqlToken> in, String table){
		return ts(in,table,0);
		}
	// table search (returns results)
	public ResultSet tableSearch(List<mysqlToken> in, String table, int limit){
		return ts(in,table,limit);
		}
	//table search (returns results)
	public boolean tableHolds(List<mysqlToken> in, String table){
		try{
			ResultSet res = ts(in,table,1);
		
			return res.first();
			}
		catch (Exception ex){
			return false;	
			}
		}
	// In thought, this .type() could prolly be changed to .compare() to get the
	// the class's comparator e.g. INT gets = , VARCHAR get LIKE
	private ResultSet ts(List<mysqlToken> in, String table, int limit){
		String qString = "";
		qString += "SELECT *";
		qString += " FROM `"+table+"` WHERE ";
		
		for (int i = 0; i < in.size(); i++)
			{
			if (i > 0)
				qString += "AND";
				
			qString += " `"+in.get(i).columnName()+"` "+in.get(i).search()+" '"
											+replace(in.get(i).value(),"'","\'")+"' ";
			}
		
		return query(qString);	
		}
	/*----------
	 * Other functions which are around
	 */
	public void close(){ try{con.close();}catch(Exception ex){}}
	
	public static void main(String args[]){
		for (int i = 4; i < 5; i++){
			/*StringPatterns sp = new StringPatterns("dictionary","am-eng-words",300,i);
			
			ListIterator<StringPart> l = (sp.patterns(60)).listIterator();
			
			while(l.hasNext()){
				StringPart temp = l.next();
				System.out.println("?"+temp.value());
				}*/
			}
		}
	}

package mysql;

public class mysqlToken {
	private String Row, Value, Type;
	private boolean Nu;
	
	public mysqlToken(String row){
		setup(row,null,null,true);
		}
	
	public mysqlToken(String row, String value){
		setup(row,value,null,true);
		}
	
	public mysqlToken(String row, String value, String type){
		setup(row,value,type,true);
		}
	
	public mysqlToken(String row, String value, String type, boolean nUll){
		setup(row,value,type,nUll);
		}
	
	private void setup(String row, String value, String type, boolean nUll){
		Row = row;
		Value = value;
		Type = type;
		Nu = nUll;
		}
	
	public String columnName(){
		return Row;
		}
	
	public String value(){
		return Value;
		}
	
	public String type(){
		return Type;
		}
	
	public boolean canBeNull(){
		return Nu;
		}
	
	public String search(){
		return "=";
		}
}

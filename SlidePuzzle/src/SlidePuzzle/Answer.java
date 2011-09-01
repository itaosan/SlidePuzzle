package SlidePuzzle;

public class Answer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Question a = new Question(3, 3, "aiueo");
		System.out.println(a.getData());
		
	}

}

class Question{
	private int row;
	private int col;
	private String data;
	
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Question(int row,int col,String data){
		this.row = row;
		this.col = col;
		this.data = data;
	}
}
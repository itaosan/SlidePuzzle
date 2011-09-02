package SlidePuzzle;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Answer {

	/**
	 * hogeee
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		int cntL = 0;
		int cntR = 0;
		int cntU = 0;
		int cntD = 0;

		int cntQuestion = 0;

		int no = 0;

		ArrayList<Question> qList = new ArrayList<Question>();
		try {
			/* ファイルをオープンします。 */
			BufferedReader br = new BufferedReader(new FileReader(
					"problems.txt"));

			/* ファイルを読み込みます。 */
			String line = "";
			int readCnt = 0;
			while ((line = br.readLine()) != null) {
				readCnt++;
				if (readCnt == 1) {
					// 1行目はスペース区切りで使えるオペレーションの数
					// 左、右、上、下
					String useCount[] = line.split(" ");
					cntL = Integer.parseInt(useCount[0]);
					cntR = Integer.parseInt(useCount[1]);
					cntU = Integer.parseInt(useCount[2]);
					cntD = Integer.parseInt(useCount[3]);
				} else if (readCnt == 2) {
					// 2行目は問題数
					cntQuestion = Integer.parseInt(line);

				} else {
					// 3行目以降は問題
					// カンマ区切りでパネル幅、パネル高さ、問題
					no++;
					String data[] = line.split(",");
					int width = Integer.parseInt(data[0]);
					int height = Integer.parseInt(data[1]);
					String qData = data[2];
					Question qes = new Question(no, height, width, qData);
					qList.add(qes);
					qes.printPanel();

				}
			}
			System.out.println("L=" + cntL + " R=" + cntR + " U=" + cntU
					+ " D=" + cntD);
			System.out.println("question=" + cntQuestion);
			
			/* ファイルをクローズします。 */
			br.close();
		} catch (IOException e) {
			System.out.println(e + "例外が発生しました");
		}

		// 問題解答ロジック

		// 回答出力部
		try {
			/* FileWriter クラスのインスタンスを作成します。 */
			FileWriter fw = new FileWriter("./answer.txt");
			/* ファイルに書き込みます。 */
			fw.write("write text !");
			/* ファイルをクローズします。 */
			fw.close();
		} catch (IOException e) {
			System.out.println(e + "例外が発生しました");
		}
	}

}

// 問題クラス
class Question {
	// 問題No
	private int no;
	// 問題level
	private int level;

	// パネル高さ
	private int height;
	// パネル幅
	private int width;
	// 問題配列
	// 問題データ
	private String data;
	// パネル
	private String[][] panel;

	public Question(int no, int height, int width, String data) {
		this.setNo(no);
		this.height = height;
		this.width = width;
		// パネルの高さ*幅をレベルとする
		this.setLevel(height * width);
		//パネル作成
		this.data = data;
		panel = new String[height][width];
		int h=0;
		int w=0;
		for(int i = 0; i < data.length(); i++) {
			panel[h][w]=String.valueOf(data.charAt(i));
			if(w<width-1){
				w++;
			}else{
				w=0;
				h++;
			}
		}
	}
	
	public void printPanel(){
		System.out.println("============= Question " + no + " Level " + level + " panel " + height + "*" + width + " ===============");
		for(int i=0;i<height;i++){
			String line=new String();
			for(int j=0;j<width;j++){
				line = line +panel[i][j];
			}
			System.out.println(line);
		}
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}
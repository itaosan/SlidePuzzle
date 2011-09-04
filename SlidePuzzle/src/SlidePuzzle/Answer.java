package SlidePuzzle;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
		List<Question> qList = new ArrayList<Question>();
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

		// level順ソート
		Collections.sort(qList, new LevelSort());

		// 問題解答ロジック
		//Question q = qList.get(3);
		Question q;
		for(int i=0;i<cntQuestion;i++){
			q= qList.get(i);
			q.solve(cntL, cntR, cntU, cntD);
			if (q.isCorrect()){
				// 正解時残りカウント更新
				cntL = q.getLeft();
				cntR = q.getRight();
				cntU = q.getUp();
				cntD = q.getDown();
			}
		}
		//No順ソート
		Collections.sort(qList, new NoSort());


		int ansCnt = 0;
		// 回答出力部
		try {
			/* FileWriter クラスのインスタンスを作成します。 */
			FileWriter fw = new FileWriter("./answer.txt");
			/* ファイルに書き込みます。 */
			for(int i=0;i<cntQuestion;i++){
				q = qList.get(i);
				if(q.isCorrect()){
					fw.write(q.getAnswer()+"\n");
					ansCnt++;
				}else{
					fw.write("\n");
				}
			}
			/* ファイルをクローズします。 */
			fw.close();
		} catch (IOException e) {
			System.out.println(e + "例外が発生しました");
		}
		System.out.println("ans cnt ="+ ansCnt);
	}

	

}

// 問題No順にソートするクラス
class NoSort implements Comparator<Question> {

	public int compare(Question o1, Question o2) {
		return o1.getNo() - o2.getNo();
	}
}

// Level順にソートするクラス
class LevelSort implements Comparator<Question> {

	public int compare(Question o1, Question o2) {
		return o1.getLevel() - o2.getLevel();
	}
}

// 問題クラス
class Question {
	// 問題No
	private int no;
	// 問題level
	private int level;

	// 現在の空白ポジション
	private int posH;
	private int posW;

	// パネル高さ
	private int height;
	// パネル幅
	private int width;
	// 問題配列
	// 問題データ
	private String data;
	// パネル
	private String[][] panel;
	//正解フラグ
	private boolean correct;
	// 回答コマンド
	private StringBuffer answer;

	// 最後に実行したコマンド
	private String lastCommand;

	// 残り移動回数
	private int left;
	private int right;
	private int up;
	private int down;

	private static final String ANS_PANEL = "123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ0";
	public static final String CMD_UP = "U";
	public static final String CMD_DOWN = "D";
	public static final String CMD_LEFT = "L";
	public static final String CMD_RIGHT = "R";

	public Question(int no, int height, int width, String data) {
		this.setNo(no);
		this.height = height;
		this.width = width;
		// パネルの高さ*幅をレベルとする
		this.setLevel(height * width);
		//不正解状態
		this.correct =false;
		// パネル作成
		this.data = data;
		panel = new String[height][width];
		int h = 0;
		int w = 0;
		for (int i = 0; i < data.length(); i++) {
			panel[h][w] = String.valueOf(data.charAt(i));
			if (panel[h][w].equals("0")) {
				posH = h;
				posW = w;
			}
			if (w < width - 1) {
				w++;
			} else {
				w = 0;
				h++;
			}
		}
		// 回答初期化
		answer = new StringBuffer();
	}

	public void printPanel() {
		System.out.println("====== Q " + no + " Level " + level + " panel "
				+ width + "*" + height + " posH=" + posH + " posW=" + posW
				+ " Up=" + up + " Down=" + down + " Left=" + left + " Right="
				+ right + "===============");
		for (int i = 0; i < height; i++) {
			String line = new String();
			for (int j = 0; j < width; j++) {
				line = line + panel[i][j];
			}
			System.out.println(line);
		}

	}

	// 解答ロジック
	public boolean solve(int left, int right, int up, int down) {
		// 残パネルセット
		this.left = left;
		this.right = right;
		this.up = up;
		this.down = down;
		lastCommand = "";
		try {
			printPanel();

			int opCnt = 0;
			while (true) {
				opCnt++;
				if (opCnt > 500) {
					System.out.println("MAX OPERATION!!");
					break;
				}
				int goUp, goDown, goLeft, goRight;
				goUp = getRank(CMD_UP,posH,posW,false);
				goDown = getRank(CMD_DOWN,posH,posW,false);
				goLeft=getRank(CMD_LEFT,posH,posW,false);
				goRight = getRank(CMD_RIGHT,posH,posW,false);
				if (goUp <= 0 && goDown <= 0 && goLeft <= 0 && goRight <= 0 && posH == height-1 && posW == width-1) {
					System.out.println("end operation");
					//正解チェック
					if (chk()){
						correct = true;
					}
					break;
				}
				int[] list = { goUp, goLeft, goDown, goRight };
				Arrays.sort(list);
				int val = list[list.length - 1];
				if (val == goUp) {
					Up();
				} else if (val == goLeft) {
					Left();
				} else if (val == goDown) {
					Down();
				} else if (val == goRight) {
					Right();
				} else {
					throw new Exception("No move!?!?!");
				}

			}
			printPanel();
			System.out.println(getAnswer());
			// System.out.println("up" + getUpRank());
			// System.out.println("down" + getDownRank());
			// System.out.println("left" + getLeftRank());
			// System.out.println("right" + getRightRank());

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	//今のパネルが正解かどうかチェック
	private boolean chk(){
		int i = 0;
		String val;
		for (int h=0;h<height-1;h++){
			for (int w=0;width<width-1;w++){
				val = panel[h][w];
				if(val.equals("=") || val.equals("0")){
				}else{
					if(val.equals(String.valueOf(ANS_PANEL.charAt(i)))==false){
						return false;
					}
				}
				i++;
			}
		}
		return true;
	}
	
	// 移動ロジック
	private void Up() throws Exception {
		String work;
		if (posH == 0) {
			throw new Exception("Can't Up! H=" + posH + "posW=" + posW);
		}
		work = panel[posH][posW];
		panel[posH][posW] = panel[posH - 1][posW];
		panel[posH - 1][posW] = work;
		// 現在のポジションセット
		posH--;
		// パネルの数を減らす
		this.up--;
		// 回答追加
		answer.append(CMD_UP);
		lastCommand = CMD_UP;
	}

	private void Down() throws Exception {
		String work;
		if (posH == height - 1) {
			throw new Exception("Can't Down! H=" + posH + "posW=" + posW);
		}
		work = panel[posH][posW];
		panel[posH][posW] = panel[posH + 1][posW];
		panel[posH + 1][posW] = work;
		// 現在のポジションセット
		posH++;
		// パネルの数を減らす
		this.down--;
		// 回答追加
		answer.append(CMD_DOWN);
		lastCommand = CMD_DOWN;
	}

	private void Left() throws Exception {
		String work;
		if (posW == 0) {
			throw new Exception("Can't Left! H=" + posH + "posW=" + posW);
		}
		work = panel[posH][posW];
		panel[posH][posW] = panel[posH][posW - 1];
		panel[posH][posW - 1] = work;
		// 現在のポジションセット
		posW--;
		// パネルの数を減らす
		this.left--;
		// 回答追加
		answer.append(CMD_LEFT);
		lastCommand = CMD_LEFT;

	}

	private void Right() throws Exception {
		String work;
		if (posW == width - 1) {
			throw new Exception("Can't Right! H=" + posH + "posW=" + posW);
		}
		work = panel[posH][posW];
		panel[posH][posW] = panel[posH][posW + 1];
		panel[posH][posW + 1] = work;
		// 現在のポジションセット
		posW++;
		// パネルの数を減らす
		this.right--;
		// 回答追加
		answer.append(CMD_RIGHT);
		lastCommand = CMD_RIGHT;
	}


	//Rank取得
	//左と上は再帰的に呼び出す
	private int getRank(String command,int h, int w,boolean isRecursive){
		int rtn=0;
		String val;
		//壁際
		if((command.equals(CMD_LEFT) && w == 0) ||
				(command.equals(CMD_RIGHT) && w == width-1) ||
				(command.equals(CMD_UP) && h == 0) ||
				(command.equals(CMD_DOWN) && h == height-1)){
			//再帰不可
			if(isRecursive){
				//再帰呼び出しの場合は考慮しない
				return 0;
			}else{
				return -100;
			}
		}
		//入れ替え対象pos
		//前回と逆に移動しようとしている場合はマイナスランク
		//int pena = ((height+width)/2)*-1;
		int pena = -1;
		if(command.equals(CMD_UP)){
			h--;
			if (lastCommand.equals(CMD_DOWN) && isRecursive == false){
				rtn-=pena;
			}
		}else if (command.equals(CMD_DOWN)){
			h++;
			if (lastCommand.equals(CMD_UP)&& isRecursive == false){
				rtn-=pena;
			}
		}else if(command.equals(CMD_LEFT)){
			w--;
			if (lastCommand.equals(CMD_RIGHT)&& isRecursive == false){
				rtn-=pena;
			}
		}else if(command.equals(CMD_RIGHT)){
			w++;
			if (lastCommand.equals(CMD_LEFT)&& isRecursive == false){
				rtn-=pena;
			}
		}
		val = panel[h][w];
		
		//移動対象が壁なら移動不可
		if(val.equals("=")){
			//再帰不可
			if(isRecursive){
				//再帰呼び出しの場合は考慮しない
				return 0;
			}else{
				return -100;
			}
		}
		

		//左と上は再帰でその左と上のRankも対象にする
		if(command.equals(CMD_LEFT) || command.equals(CMD_UP)){
			rtn += getRank(CMD_LEFT, h, w, true) + getRank(CMD_UP,h,w,true);
		}
		
		//現在位置からの差分を出す
		int rankH = Math.abs(h - getCorrectH(val));
		int rankW = Math.abs(w - getCorrectW(val));
		
		rtn += rankH + rankW;
		if (isRecursive){
			rtn = 0;
		}
		return rtn;
	}
	
	
	// 指定場所のあるべきパネルを返す
	private String getCorrectVal(int H, int W) {
		return String.valueOf(ANS_PANEL.charAt(H * height + W));
	}

	// 指定文字の正解の高さを返す
	private int getCorrectH(String val) {
		return ANS_PANEL.indexOf(val) / width;

	}

	// 指定文字の正解の幅を返す
	private int getCorrectW(String val) {
		return ANS_PANEL.indexOf(val) % width;

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

	public String[][] getPanel() {
		return panel;
	}

	public void setPanel(String[][] panel) {
		this.panel = panel;
	}

	public int getLeft() {
		return left;
	}

	public void setLeft(int left) {
		this.left = left;
	}

	public int getRight() {
		return right;
	}

	public void setRight(int right) {
		this.right = right;
	}

	public int getDown() {
		return down;
	}

	public void setDown(int down) {
		this.down = down;
	}

	public int getUp() {
		return up;
	}

	public void setUp(int up) {
		this.up = up;
	}

	public String getAnswer() {
		return answer.toString();
	}

	public boolean isCorrect() {
		return correct;
	}

}
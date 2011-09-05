package SlidePuzzle3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.text.html.HTMLDocument.Iterator;

public class Answer3 {

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
		 Question q = qList.get(3);
		 q.solve(cntL, cntR, cntU, cntD);

//		Question q;
//		for (int i = 0; i < cntQuestion; i++) {
//			q = qList.get(i);
//			q.solve(cntL, cntR, cntU, cntD);
//			if (q.isCorrect()) {
//				// 正解時残りカウント更新
//				cntL = q.getLeft();
//				cntR = q.getRight();
//				cntU = q.getUp();
//				cntD = q.getDown();
//			}
//		}
		// No順ソート
		Collections.sort(qList, new NoSort());

		int ansCnt = 0;
		Map<String, Ansdata> li = new HashMap<String, Ansdata>();
		// 回答出力部
		try {
			/* FileWriter クラスのインスタンスを作成します。 */
			FileWriter fw = new FileWriter("./answer.txt");
			/* ファイルに書き込みます。 */
			for (int i = 0; i < cntQuestion; i++) {
				q = qList.get(i);
				Ansdata ans = new Ansdata(q.getHeight(), q.getWidth(),
						q.getWall());
				Ansdata wk = li.get(ans.getKey());
				if (wk == null) {
					ans.setQuesCnt(1);
					if (q.isCorrect()) {
						ans.setAnsCnt(1);
					} else {
						ans.setAnsCnt(0);
					}
					li.put(ans.getKey(), ans);
				} else {
					wk.setQuesCnt(wk.getQuesCnt() + 1);
					if (q.isCorrect()) {
						wk.setAnsCnt(wk.getAnsCnt() + 1);
					}
					li.put(wk.getKey(), wk);
				}

				if (q.isCorrect()) {
					fw.write(q.getAnswer() + "\n");
					ansCnt++;
				} else {
					fw.write("\n");
				}
			}
			/* ファイルをクローズします。 */
			fw.close();
		} catch (IOException e) {
			System.out.println(e + "例外が発生しました");
		}
		Set<Entry<String, Ansdata>> items = li.entrySet(); // (9)MapのKeyと値のペアを代入
		// (10)Iterator処理
		for (java.util.Iterator<Entry<String, Ansdata>> i = items.iterator(); i
				.hasNext();) {
			Ansdata a = i.next().getValue();

			System.out.println(a.getKey() + "=" + a.getAnsCnt() + "/"
					+ a.getQuesCnt());
		}
		System.out.println("ans cnt =" + ansCnt);
	}

}

// 解答集計用クラス
class Ansdata {
	private int ansCnt;
	private int quesCnt;
	private String key;

	Ansdata(int h, int w, int wall) {
		key = h + "*" + w + "*" + wall;
	}

	public int getAnsCnt() {
		return ansCnt;
	}

	public void setAnsCnt(int ansCnt) {
		this.ansCnt = ansCnt;
	}

	public int getQuesCnt() {
		return quesCnt;
	}

	public void setQuesCnt(int quesCnt) {
		this.quesCnt = quesCnt;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
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
	// '='の数
	private int wall = 0;

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
	private char[] panel;
	//移動可能状態を表す 0=移動可能　1=移動不可
	private int[] status;
	// 正解フラグ
	private boolean correct;
	// 回答コマンド
	private StringBuffer answer;

	// 最後に実行したコマンド
	private char lastCommand;

	// 残り移動回数
	private int left;
	private int right;
	private int up;
	private int down;

	private static final char[] ANS_PANEL = { '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
			'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
			'X', 'Y', 'Z', '0' };
	public static final char CMD_UP = 'U';
	public static final char CMD_DOWN = 'D';
	public static final char CMD_LEFT = 'L';
	public static final char CMD_RIGHT = 'R';

	public Question(int no, int height, int width, String data) {
		this.setNo(no);
		this.height = height;
		this.width = width;
		// 不正解状態
		this.correct = false;
		// パネル作成
		this.data = data;
		panel = new char[data.length()];
		for (int i = 0; i < data.length(); i++) {
			panel[i] = data.charAt(i);
			if (panel[i] == '=') {
				wall++;
			}
			if (panel[i] == '0') {
				posH = i / width;
				posW = i % width;
			}
		}
		//移動可否ステータス
		status = new int[data.length()];
		//初期は移動可能
		for (int i = 0; i < data.length(); i++) {
			status[i] = 0;
			if (panel[i] == '=') {
				//壁の場合は移動不可
				status[i] = 1;
			}
		}
		
		
		// パネルの高さ*幅×壁の数をレベルとする
		this.setLevel(height * width * (wall + 1));
		// 回答初期化
		answer = new StringBuffer();
	}

	public void printPanel() {
		System.out.println("====== Q " + no + " Level " + level + " panel "
				+ width + "*" + height + "*" + wall + " posH=" + posH
				+ " posW=" + posW + " Up=" + up + " Down=" + down + " Left="
				+ left + " Right=" + right + "===============");
		for (int i = 0; i < height; i++) {
			StringBuffer line = new StringBuffer();
			for (int j = 0; j < width; j++) {
				line = line.append(panel[i * width + j]);
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
		
		try {
			printPanel();

			int opCnt = 0;
			while (true) {
				opCnt++;
				if (opCnt > 1000) {
					System.out.println("MAX OPERATION!!");
					break;
				}
				
				//1が入っている？
				//まずは1を所定の位置に
				batchMove('1', 0, 0);
				setStatus(0,0,1);
				printPanel();
				//3を2の位置にセット
				batchMove('3', 0, 1);
				printPanel();
				//2を３の下にセット（3を動かさないように）
				batchMove('2', 1, 1);
				printPanel();
				//3の位置から左、下へ移動
				moveBlankPanel(0, 2);
				printPanel();
				move(CMD_LEFT);
				printPanel();
				move(CMD_DOWN);
				printPanel();
				setStatus(0,1,1);
				setStatus(0,2,1);
				
				//７を4の位置にセット
				batchMove('7', 1, 0);
				setStatus(1,0,1);
				//４」を７の右にセット（7を動かさないように）
				batchMove('4', 1, 1);
				setStatus(1,1,1);
				//7の位置から上、右へ移動
				moveBlankPanel(2, 0);
				setStatus(1,0,0);
				setStatus(1,1,0);
				move(CMD_UP);
				move(CMD_RIGHT);
				//回転して0を右下にして、解答チェック
				break;

			}
			printPanel();
			System.out.println(getAnswer());

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * 指定パネルを指定位置に移動させるバッチ
	 * @param val　指定パネル
	 * @param targetH　指定高さ
	 * @param targetW　指定横
	 * @throws Exception
	 */
	private boolean batchMove(char val,int targetH,int targetW) throws Exception{
		int brankTargetH = 0;
		int brankTargetW = 0;
		int costUp = 0;
		int costDown = 0;
		int costLeft = 0;
		int costRight = 0;
		
		//指定パネルの現在位置
		int nowH = getNowHeightOf(val);
		int nowW = getNowWidthOf(val);
		//指定位置への距離
		int rangeH = nowH-targetH;
		int rangeW = nowW-targetW;

		if(rangeH ==0 && rangeW ==0){
			//移動の必要なし
			return true;
		}
		
		if(rangeH > 0){
			//上に移動したい
			//空白を上のマスに持ってくるコスト計算
			costUp = getCostOfBlankMove(val,CMD_UP);
		}else if (rangeH < 0){
			//下に移動したい
			//空白を下のマスに持ってくるコスト計算
			costDown = getCostOfBlankMove(val,CMD_DOWN);
		}

		if(rangeW > 0){
			//左に移動したい
			//空白を左のマスに持ってくるコスト計算
			costLeft = getCostOfBlankMove(val,CMD_LEFT);
		}else if (rangeW < 0){
			//右に移動したい
			//空白を右のマスに持ってくるコスト計算
			costRight = getCostOfBlankMove(val,CMD_RIGHT);
		}
		
		int[] list = {costUp,costDown,costLeft,costRight};
		Arrays.sort(list);
		int ope=0;
		//0以外の最小コストのオペレーションをする
		for(int i=0 ; i < list.length;i++){
			ope= list[i];
			if (ope > 0){
				break;
			}
		}
		if (ope == costUp) {
			//上に動かしたいので空白を上へ
			brankTargetH = nowH-1;
			brankTargetW = nowW;
			//同軸だったら左か右に1マス動かして最後に戻す(右優先)
			if(posW == targetW && posH>targetH){
				if(posW==width){
					move(CMD_LEFT);
				}else{
					move(CMD_RIGHT);
				}
			}
			//空白を指定位置に
			moveBlankPanel(brankTargetH, brankTargetW);
			//空白を下に動かし対象パネルを上に動かす
			move(CMD_DOWN);
		} else if (ope == costDown) {
			//下に動かしたいので空白を下へ
			brankTargetH = nowH+1;
			brankTargetW = nowW;
			//同軸だったら左か右に1マス動かして最後に戻す(右優先)
			if(posW == targetW && posH<targetH){
				if(posW==width){
					move(CMD_LEFT);
				}else{
					move(CMD_RIGHT);
				}
			}
			//空白を指定位置に
			moveBlankPanel(brankTargetH, brankTargetW);
			//空白を上に動かし対象パネルを下に動かす
			move(CMD_UP);
		} else if (ope == costLeft) {
			//左に動かしたいので空白を左へ
			brankTargetH = nowH;
			brankTargetW = nowW -1;
			//同軸だったら上か下に1マス動かして最後に戻す(下優先)
			if(posH == targetH && posW>targetW){
				if(posH==height){
					move(CMD_UP);
				}else{
					move(CMD_DOWN);
				}
			}
			//空白を指定位置に
			moveBlankPanel(brankTargetH, brankTargetW);
			//空白を右に動かし対象パネルを左に動かす
			move(CMD_RIGHT);
		} else if (ope == costRight) {
			//右に動かしたいので空白を右へ
			brankTargetH = nowH;
			brankTargetW = nowW +1;
			//同軸だったら上か下に1マス動かして最後に戻す(下優先)
			if(posH == targetH && posW<targetW){
				if(posH==height){
					move(CMD_UP);
				}else{
					move(CMD_DOWN);
				}
			}
			//空白を指定位置に
			moveBlankPanel(brankTargetH, brankTargetW);
			//空白を左に動かし対象パネルを右に動かす
			move(CMD_LEFT);
		} 
		
		//移動の必要がなくなるまで繰り返す
		return batchMove(val,targetH,targetW);
		
	}

	
	/**
	 * 空白パネルを指定位置に動かす
	 * @param targetH
	 * @param targetW
	 * @throws Exception 
	 */
	private void moveBlankPanel(int targetH,int targetW) throws Exception{
		//横移動
		int moveHorizontal = targetW - posW;
		int moveVertical = targetH -posH;
		
		if(lastCommand==CMD_LEFT ||lastCommand==CMD_RIGHT){
			//最終コマンドが横の場合は縦移動から
			//縦移動
			if (moveVertical >0){
				for(int i=0;i<Math.abs(moveVertical);i++){
					//移動可能かチェック
					//移動不可ならずれて回り道
					if(isMovable(CMD_DOWN)){
						move(CMD_DOWN);
					}else{
						//ずれて再度判定
						if(isMovable(CMD_RIGHT)){
							move(CMD_RIGHT);
						}else if(isMovable(CMD_LEFT)){
							move(CMD_LEFT);
						}else if(isMovable(CMD_UP)){
							move(CMD_UP);
						}else{
							printPanel();
							throw new Exception("Move不可！！ lastcommand" + lastCommand);
						}
						moveBlankPanel(targetH,targetW);
						return;
					}
				}
			}else{
				for(int i=0;i<Math.abs(moveVertical);i++){
					//移動可能かチェック
					//移動不可ならずれて回り道
					if(isMovable(CMD_UP)){
						move(CMD_UP);
					}else{
						//ずれて再度判定
						if(isMovable(CMD_RIGHT)){
							move(CMD_RIGHT);
						}else if(isMovable(CMD_LEFT)){
							move(CMD_LEFT);
						}else if(isMovable(CMD_DOWN)){
							move(CMD_DOWN);
						}else{
							printPanel();
							throw new Exception("Move不可！！ lastcommand" + lastCommand);
						}
						moveBlankPanel(targetH,targetW);
						return;
					}
				}
			}
			//横移動
			if (moveHorizontal >0){
				for(int i=0;i<Math.abs(moveHorizontal);i++){
					//移動可能かチェック
					//移動不可ならずれて回り道
					if(isMovable(CMD_RIGHT)){
						move(CMD_RIGHT);
					}else{
						//ずれて再度判定
						if(isMovable(CMD_UP)){
							move(CMD_UP);
						}else if(isMovable(CMD_DOWN)){
							move(CMD_DOWN);
						}else if(isMovable(CMD_LEFT)){
							move(CMD_LEFT);
						}else{
							printPanel();
							throw new Exception("Move不可！！ lastcommand" + lastCommand);
						}
						moveBlankPanel(targetH,targetW);
						return;
					}
				}
			}else{
				for(int i=0;i<Math.abs(moveHorizontal);i++){
					//移動可能かチェック
					//移動不可ならずれて回り道
					if(isMovable(CMD_LEFT)){
						move(CMD_LEFT);
					}else{
						//ずれて再度判定
						if(isMovable(CMD_UP)){
							move(CMD_UP);
						}else if(isMovable(CMD_DOWN)){
							move(CMD_DOWN);
						}else if(isMovable(CMD_RIGHT)){
							move(CMD_RIGHT);
						}else{
							printPanel();
							throw new Exception("Move不可！！ lastcommand" + lastCommand);
						}
						moveBlankPanel(targetH,targetW);
						return;
					}
				}
			}
			
		}else{
			//最終コマンドが縦の場合は横移動から
			
			//横移動
			if (moveHorizontal >0){
				for(int i=0;i<Math.abs(moveHorizontal);i++){
					//移動可能かチェック
					//移動不可ならずれて回り道
					if(isMovable(CMD_RIGHT)){
						move(CMD_RIGHT);
					}else{
						//ずれて再度判定
						if(isMovable(CMD_UP)){
							move(CMD_UP);
						}else if(isMovable(CMD_DOWN)){
							move(CMD_DOWN);
						}else if(isMovable(CMD_LEFT)){
							move(CMD_LEFT);
						}else{
							printPanel();
							throw new Exception("Move不可！！ lastcommand" + lastCommand);
						}
						moveBlankPanel(targetH,targetW);
						return;
					}
				}
			}else{
				for(int i=0;i<Math.abs(moveHorizontal);i++){
					//移動可能かチェック
					//移動不可ならずれて回り道
					if(isMovable(CMD_LEFT)){
						move(CMD_LEFT);
					}else{
						//ずれて再度判定
						if(isMovable(CMD_UP)){
							move(CMD_UP);
						}else if(isMovable(CMD_DOWN)){
							move(CMD_DOWN);
						}else if(isMovable(CMD_RIGHT)){
							move(CMD_RIGHT);
						}else{
							printPanel();
							throw new Exception("Move不可！！ lastcommand" + lastCommand);
						}
						moveBlankPanel(targetH,targetW);
						return;
					}
				}
			}
			//縦移動
			if (moveVertical >0){
				for(int i=0;i<Math.abs(moveVertical);i++){
					//移動可能かチェック
					//移動不可ならずれて回り道
					if(isMovable(CMD_DOWN)){
						move(CMD_DOWN);
					}else{
						//ずれて再度判定
						if(isMovable(CMD_RIGHT)){
							move(CMD_RIGHT);
						}else if(isMovable(CMD_LEFT)){
							move(CMD_LEFT);
						}else if(isMovable(CMD_UP)){
							move(CMD_UP);
						}else{
							printPanel();
							throw new Exception("Move不可！！ lastcommand" + lastCommand);
						}
						moveBlankPanel(targetH,targetW);
						return;
					}
				}
			}else{
				for(int i=0;i<Math.abs(moveVertical);i++){
					//移動可能かチェック
					//移動不可ならずれて回り道
					if(isMovable(CMD_UP)){
						move(CMD_UP);
					}else{
						//ずれて再度判定
						if(isMovable(CMD_RIGHT)){
							move(CMD_RIGHT);
						}else if(isMovable(CMD_LEFT)){
							move(CMD_LEFT);
						}else if(isMovable(CMD_DOWN)){
							move(CMD_DOWN);
						}else{
							printPanel();
							throw new Exception("Move不可！！ lastcommand" + lastCommand);
						}
						moveBlankPanel(targetH,targetW);
						return;
					}
				}
			}
		}

	}
	
	/**
	 * 空白を指定文字の指定隣接場所に移動するコスト計算
	 * @param targetVal　指定文字
	 * @param pos　指定隣接場所　CMD_XX
	 * @return　コスト
	 */
	private int getCostOfBlankMove(char targetVal,char pos){
		int cost = 0;
		int targetH = getNowHeightOf(targetVal);
		int targetW = getNowWidthOf(targetVal);
		if (pos == CMD_UP){
			targetH--;
		}else if(pos == CMD_DOWN){
			targetH++;
		}else if(pos == CMD_LEFT){
			targetW--;
		}else if(pos == CMD_RIGHT){
			targetW++;
		}
		//単純位置からコスト算出
		cost = Math.abs(posW - targetW)+Math.abs(posH - targetH);
		//回り道コスト
		if (pos == CMD_UP){
			if(posW == targetW && posH>targetH){
				cost +=2;
			}
		}else if(pos == CMD_DOWN){
			if(posW == targetW && posH<targetH){
				cost +=2;
			}
		}else if(pos == CMD_LEFT){
			if(posH == targetH && posW>targetW){
				cost +=2;
			}
		}else if(pos == CMD_RIGHT){
			if(posH == targetH && posW<targetW){
				cost +=2;
			}
		}
		return cost;
	}
	
	//指定位置のパネルを指定位置に移動させるバッチ
	private void batchMove(int h,int w,int targetH,int targetW) throws Exception{
		batchMove(getPiece(h,w),targetH,targetW);
	}

	//指定位置の値を取得
	private char getPiece(int h,int w){
		return panel[h*width+w];
	}
	/**
	 * 今のパネルが正解かどうかチェック
	 * @return
	 */
	private boolean chk() {
		for (int i = 0; i < panel.length; i++) {
			if (panel[i] == '=' || panel[i] == '0') {
			} else {
				if (panel[i] != ANS_PANEL[i]) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 移動可能かどうかのステータスを返す
	 * @param h
	 * @param w
	 * @return
	 */
	private int getStatus(int h,int w){
		return status[h*width+w];
	}
	
	private void setStatus(int h,int w,int val){
		status[h*width+w] = val;
	}
	
	/**
	 * 移動可能かチェック
	 * @param command
	 * @return
	 * @throws Exception
	 */
	private boolean isMovable(char command)throws Exception{
		int moveH =0;
		int moveW = 0;
		if (command == CMD_UP) {
			if (posH == 0) {
				System.out.println("Can't Up! H=" + posH + "posW=" + posW);
				return false;
			}
			//前回移動と逆コマンドはダメ
			if(lastCommand == CMD_DOWN){
				return false;
			}
			moveH = posH - 1;
			moveW = posW;
		} else if (command == CMD_DOWN) {
			if (posH == height - 1) {
				System.out.println("Can't Down! H=" + posH + "posW=" + posW);
				return false;
			}
			//前回移動と逆コマンドはダメ
			if(lastCommand == CMD_UP){
				return false;
			}
			moveH = posH + 1;
			moveW = posW;
		} else if (command == CMD_LEFT) {
			if (posW == 0) {
				System.out.println("Can't Left! H=" + posH + "posW=" + posW);
				return false;
			}
			//前回移動と逆コマンドはダメ
			if(lastCommand == CMD_RIGHT){
				return false;
			}
			moveH = posH;
			moveW = posW - 1;
		} else if (command == CMD_RIGHT) {
			if (posW == width - 1) {
				System.out.println("Can't Right! H=" + posH + "posW=" + posW);
				return false;
			}
			//前回移動と逆コマンドはダメ
			if(lastCommand == CMD_LEFT){
				return false;
			}
			moveH = posH;
			moveW = posW + 1;
		}
		
		
		if(getStatus(moveH, moveW) == 0){
			return true;
		}else{
			return false;
		}
	}
	
	// 移動ロジック
	private void move(char command) throws Exception {
		char work;
		int moveH = -1;
		int moveW = -1;
		
		if (command == CMD_UP) {
			moveH = posH - 1;
			moveW = posW;
			// パネルの数を減らす
			this.up--;
		} else if (command == CMD_DOWN) {
			moveH = posH + 1;
			moveW = posW;
			// パネルの数を減らす
			this.down--;
		} else if (command == CMD_LEFT) {
			moveH = posH;
			moveW = posW - 1;
			// パネルの数を減らす
			this.left--;
		} else if (command == CMD_RIGHT) {
			moveH = posH;
			moveW = posW + 1;
			// パネルの数を減らす
			this.right--;
		}
		work = panel[posH * width + posW];
		panel[posH * width + posW] = panel[moveH * width + moveW];
		panel[moveH * width + moveW] = work;
		// 現在のポジションセット
		posH = moveH;
		posW = moveW;
		// 回答追加
		answer.append(command);
		lastCommand = command;
	}

	// Rank取得
	// 左と上は再帰的に呼び出す
	private int getRank(char command, int h, int w, boolean isRecursive) {
		int rtn = 0;
		char val;
		// 壁際
		if ((command == CMD_LEFT && w == 0)
				|| (command == CMD_RIGHT && w == width - 1)
				|| (command == CMD_UP && h == 0)
				|| (command == CMD_DOWN && h == height - 1)) {
			// 再帰不可
			if (isRecursive) {
				// 再帰呼び出しの場合は考慮しない
				return 0;
			} else {
				return -100;
			}
		}
		// 入れ替え対象pos
		// 前回と逆に移動しようとしている場合はマイナスランク
		// int pena = ((height+width)/2)*-1;
		int pena = 5;
		if (command == CMD_UP) {
			h--;
			if (lastCommand == CMD_DOWN && isRecursive == false) {
				rtn -= pena;
			}
		} else if (command == CMD_DOWN) {
			h++;
			if (lastCommand == CMD_UP && isRecursive == false) {
				rtn -= pena;
			}
		} else if (command == CMD_LEFT) {
			w--;
			if (lastCommand == CMD_RIGHT && isRecursive == false) {
				rtn -= pena;
			}
		} else if (command == CMD_RIGHT) {
			w++;
			if (lastCommand == CMD_LEFT && isRecursive == false) {
				rtn -= pena;
			}
		}
		val = panel[h * width + w];

		// 移動対象が壁なら移動不可
		if (val == '=') {
			// 再帰不可
			if (isRecursive) {
				// 再帰呼び出しの場合は考慮しない
				return 0;
			} else {
				return -100;
			}
		}

		// 左と上は再帰でその左と上のRankも対象にする
		if (command == CMD_LEFT || command == CMD_UP) {
			rtn += getRank(CMD_LEFT, h, w, true) + getRank(CMD_UP, h, w, true);
		}

		// 現在位置からの差分を出す
		int rankH = Math.abs(h - getCorrectH(val));
		int rankW = Math.abs(w - getCorrectW(val));

		rtn += rankH + rankW;
		if (isRecursive) {
			rtn = 0;
		}
		return rtn;
	}

	/**
	 * 指定文字の現在高さを返す
	 * @param val
	 * @return
	 */
	private int getNowHeightOf(char val){
		return String.valueOf(panel).indexOf(val) / width;
	}

	/**
	 * 指定文字の現在幅を返す
	 * @param val
	 * @return
	 */
	private int getNowWidthOf(char val){
		return String.valueOf(panel).indexOf(val) % width;
	}

	// 指定場所のあるべきパネルを返す
	private char getCorrectVal(int h, int w) {
		return ANS_PANEL[h * width + w];
	}

	// 指定文字の正解の高さを返す
	private int getCorrectH(char val) {
		return String.valueOf(ANS_PANEL).indexOf(val) / width;

	}

	// 指定文字の正解の幅を返す
	private int getCorrectW(char val) {
		return String.valueOf(ANS_PANEL).indexOf(val) % width;

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

	public char[] getPanel() {
		return panel;
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

	public int getWall() {
		return wall;
	}

	public void setWall(int wall) {
		this.wall = wall;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

}
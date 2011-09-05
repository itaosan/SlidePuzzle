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
			/* �t�@�C�����I�[�v�����܂��B */
			BufferedReader br = new BufferedReader(new FileReader(
					"problems.txt"));

			/* �t�@�C����ǂݍ��݂܂��B */
			String line = "";
			int readCnt = 0;
			while ((line = br.readLine()) != null) {
				readCnt++;
				if (readCnt == 1) {
					// 1�s�ڂ̓X�y�[�X��؂�Ŏg����I�y���[�V�����̐�
					// ���A�E�A��A��
					String useCount[] = line.split(" ");
					cntL = Integer.parseInt(useCount[0]);
					cntR = Integer.parseInt(useCount[1]);
					cntU = Integer.parseInt(useCount[2]);
					cntD = Integer.parseInt(useCount[3]);
				} else if (readCnt == 2) {
					// 2�s�ڂ͖�萔
					cntQuestion = Integer.parseInt(line);

				} else {
					// 3�s�ڈȍ~�͖��
					// �J���}��؂�Ńp�l�����A�p�l�������A���
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

			/* �t�@�C�����N���[�Y���܂��B */
			br.close();
		} catch (IOException e) {
			System.out.println(e + "��O���������܂���");
		}

		// level���\�[�g
		Collections.sort(qList, new LevelSort());

		// ���𓚃��W�b�N
		 Question q = qList.get(3);
		 q.solve(cntL, cntR, cntU, cntD);

//		Question q;
//		for (int i = 0; i < cntQuestion; i++) {
//			q = qList.get(i);
//			q.solve(cntL, cntR, cntU, cntD);
//			if (q.isCorrect()) {
//				// �������c��J�E���g�X�V
//				cntL = q.getLeft();
//				cntR = q.getRight();
//				cntU = q.getUp();
//				cntD = q.getDown();
//			}
//		}
		// No���\�[�g
		Collections.sort(qList, new NoSort());

		int ansCnt = 0;
		Map<String, Ansdata> li = new HashMap<String, Ansdata>();
		// �񓚏o�͕�
		try {
			/* FileWriter �N���X�̃C���X�^���X���쐬���܂��B */
			FileWriter fw = new FileWriter("./answer.txt");
			/* �t�@�C���ɏ������݂܂��B */
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
			/* �t�@�C�����N���[�Y���܂��B */
			fw.close();
		} catch (IOException e) {
			System.out.println(e + "��O���������܂���");
		}
		Set<Entry<String, Ansdata>> items = li.entrySet(); // (9)Map��Key�ƒl�̃y�A����
		// (10)Iterator����
		for (java.util.Iterator<Entry<String, Ansdata>> i = items.iterator(); i
				.hasNext();) {
			Ansdata a = i.next().getValue();

			System.out.println(a.getKey() + "=" + a.getAnsCnt() + "/"
					+ a.getQuesCnt());
		}
		System.out.println("ans cnt =" + ansCnt);
	}

}

// �𓚏W�v�p�N���X
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

// ���No���Ƀ\�[�g����N���X
class NoSort implements Comparator<Question> {

	public int compare(Question o1, Question o2) {
		return o1.getNo() - o2.getNo();
	}
}

// Level���Ƀ\�[�g����N���X
class LevelSort implements Comparator<Question> {

	public int compare(Question o1, Question o2) {
		return o1.getLevel() - o2.getLevel();
	}
}

// ���N���X
class Question {
	// ���No
	private int no;
	// ���level
	private int level;
	// '='�̐�
	private int wall = 0;

	// ���݂̋󔒃|�W�V����
	private int posH;
	private int posW;

	// �p�l������
	private int height;
	// �p�l����
	private int width;
	// ���z��
	// ���f�[�^
	private String data;
	// �p�l��
	private char[] panel;
	//�ړ��\��Ԃ�\�� 0=�ړ��\�@1=�ړ��s��
	private int[] status;
	// �����t���O
	private boolean correct;
	// �񓚃R�}���h
	private StringBuffer answer;

	// �Ō�Ɏ��s�����R�}���h
	private char lastCommand;

	// �c��ړ���
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
		// �s�������
		this.correct = false;
		// �p�l���쐬
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
		//�ړ��ۃX�e�[�^�X
		status = new int[data.length()];
		//�����͈ړ��\
		for (int i = 0; i < data.length(); i++) {
			status[i] = 0;
			if (panel[i] == '=') {
				//�ǂ̏ꍇ�͈ړ��s��
				status[i] = 1;
			}
		}
		
		
		// �p�l���̍���*���~�ǂ̐������x���Ƃ���
		this.setLevel(height * width * (wall + 1));
		// �񓚏�����
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

	// �𓚃��W�b�N
	public boolean solve(int left, int right, int up, int down) {
		// �c�p�l���Z�b�g
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
				
				//1�������Ă���H
				//�܂���1������̈ʒu��
				batchMove('1', 0, 0);
				setStatus(0,0,1);
				printPanel();
				//3��2�̈ʒu�ɃZ�b�g
				batchMove('3', 0, 1);
				printPanel();
				//2���R�̉��ɃZ�b�g�i3�𓮂����Ȃ��悤�Ɂj
				batchMove('2', 1, 1);
				printPanel();
				//3�̈ʒu���獶�A���ֈړ�
				moveBlankPanel(0, 2);
				printPanel();
				move(CMD_LEFT);
				printPanel();
				move(CMD_DOWN);
				printPanel();
				setStatus(0,1,1);
				setStatus(0,2,1);
				
				//�V��4�̈ʒu�ɃZ�b�g
				batchMove('7', 1, 0);
				setStatus(1,0,1);
				//�S�v���V�̉E�ɃZ�b�g�i7�𓮂����Ȃ��悤�Ɂj
				batchMove('4', 1, 1);
				setStatus(1,1,1);
				//7�̈ʒu�����A�E�ֈړ�
				moveBlankPanel(2, 0);
				setStatus(1,0,0);
				setStatus(1,1,0);
				move(CMD_UP);
				move(CMD_RIGHT);
				//��]����0���E���ɂ��āA�𓚃`�F�b�N
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
	 * �w��p�l�����w��ʒu�Ɉړ�������o�b�`
	 * @param val�@�w��p�l��
	 * @param targetH�@�w�荂��
	 * @param targetW�@�w�艡
	 * @throws Exception
	 */
	private boolean batchMove(char val,int targetH,int targetW) throws Exception{
		int brankTargetH = 0;
		int brankTargetW = 0;
		int costUp = 0;
		int costDown = 0;
		int costLeft = 0;
		int costRight = 0;
		
		//�w��p�l���̌��݈ʒu
		int nowH = getNowHeightOf(val);
		int nowW = getNowWidthOf(val);
		//�w��ʒu�ւ̋���
		int rangeH = nowH-targetH;
		int rangeW = nowW-targetW;

		if(rangeH ==0 && rangeW ==0){
			//�ړ��̕K�v�Ȃ�
			return true;
		}
		
		if(rangeH > 0){
			//��Ɉړ�������
			//�󔒂���̃}�X�Ɏ����Ă���R�X�g�v�Z
			costUp = getCostOfBlankMove(val,CMD_UP);
		}else if (rangeH < 0){
			//���Ɉړ�������
			//�󔒂����̃}�X�Ɏ����Ă���R�X�g�v�Z
			costDown = getCostOfBlankMove(val,CMD_DOWN);
		}

		if(rangeW > 0){
			//���Ɉړ�������
			//�󔒂����̃}�X�Ɏ����Ă���R�X�g�v�Z
			costLeft = getCostOfBlankMove(val,CMD_LEFT);
		}else if (rangeW < 0){
			//�E�Ɉړ�������
			//�󔒂��E�̃}�X�Ɏ����Ă���R�X�g�v�Z
			costRight = getCostOfBlankMove(val,CMD_RIGHT);
		}
		
		int[] list = {costUp,costDown,costLeft,costRight};
		Arrays.sort(list);
		int ope=0;
		//0�ȊO�̍ŏ��R�X�g�̃I�y���[�V����������
		for(int i=0 ; i < list.length;i++){
			ope= list[i];
			if (ope > 0){
				break;
			}
		}
		if (ope == costUp) {
			//��ɓ����������̂ŋ󔒂����
			brankTargetH = nowH-1;
			brankTargetW = nowW;
			//�����������獶���E��1�}�X�������čŌ�ɖ߂�(�E�D��)
			if(posW == targetW && posH>targetH){
				if(posW==width){
					move(CMD_LEFT);
				}else{
					move(CMD_RIGHT);
				}
			}
			//�󔒂��w��ʒu��
			moveBlankPanel(brankTargetH, brankTargetW);
			//�󔒂����ɓ������Ώۃp�l������ɓ�����
			move(CMD_DOWN);
		} else if (ope == costDown) {
			//���ɓ����������̂ŋ󔒂�����
			brankTargetH = nowH+1;
			brankTargetW = nowW;
			//�����������獶���E��1�}�X�������čŌ�ɖ߂�(�E�D��)
			if(posW == targetW && posH<targetH){
				if(posW==width){
					move(CMD_LEFT);
				}else{
					move(CMD_RIGHT);
				}
			}
			//�󔒂��w��ʒu��
			moveBlankPanel(brankTargetH, brankTargetW);
			//�󔒂���ɓ������Ώۃp�l�������ɓ�����
			move(CMD_UP);
		} else if (ope == costLeft) {
			//���ɓ����������̂ŋ󔒂�����
			brankTargetH = nowH;
			brankTargetW = nowW -1;
			//������������ォ����1�}�X�������čŌ�ɖ߂�(���D��)
			if(posH == targetH && posW>targetW){
				if(posH==height){
					move(CMD_UP);
				}else{
					move(CMD_DOWN);
				}
			}
			//�󔒂��w��ʒu��
			moveBlankPanel(brankTargetH, brankTargetW);
			//�󔒂��E�ɓ������Ώۃp�l�������ɓ�����
			move(CMD_RIGHT);
		} else if (ope == costRight) {
			//�E�ɓ����������̂ŋ󔒂��E��
			brankTargetH = nowH;
			brankTargetW = nowW +1;
			//������������ォ����1�}�X�������čŌ�ɖ߂�(���D��)
			if(posH == targetH && posW<targetW){
				if(posH==height){
					move(CMD_UP);
				}else{
					move(CMD_DOWN);
				}
			}
			//�󔒂��w��ʒu��
			moveBlankPanel(brankTargetH, brankTargetW);
			//�󔒂����ɓ������Ώۃp�l�����E�ɓ�����
			move(CMD_LEFT);
		} 
		
		//�ړ��̕K�v���Ȃ��Ȃ�܂ŌJ��Ԃ�
		return batchMove(val,targetH,targetW);
		
	}

	
	/**
	 * �󔒃p�l�����w��ʒu�ɓ�����
	 * @param targetH
	 * @param targetW
	 * @throws Exception 
	 */
	private void moveBlankPanel(int targetH,int targetW) throws Exception{
		//���ړ�
		int moveHorizontal = targetW - posW;
		int moveVertical = targetH -posH;
		
		if(lastCommand==CMD_LEFT ||lastCommand==CMD_RIGHT){
			//�ŏI�R�}���h�����̏ꍇ�͏c�ړ�����
			//�c�ړ�
			if (moveVertical >0){
				for(int i=0;i<Math.abs(moveVertical);i++){
					//�ړ��\���`�F�b�N
					//�ړ��s�Ȃ炸��ĉ�蓹
					if(isMovable(CMD_DOWN)){
						move(CMD_DOWN);
					}else{
						//����čēx����
						if(isMovable(CMD_RIGHT)){
							move(CMD_RIGHT);
						}else if(isMovable(CMD_LEFT)){
							move(CMD_LEFT);
						}else if(isMovable(CMD_UP)){
							move(CMD_UP);
						}else{
							printPanel();
							throw new Exception("Move�s�I�I lastcommand" + lastCommand);
						}
						moveBlankPanel(targetH,targetW);
						return;
					}
				}
			}else{
				for(int i=0;i<Math.abs(moveVertical);i++){
					//�ړ��\���`�F�b�N
					//�ړ��s�Ȃ炸��ĉ�蓹
					if(isMovable(CMD_UP)){
						move(CMD_UP);
					}else{
						//����čēx����
						if(isMovable(CMD_RIGHT)){
							move(CMD_RIGHT);
						}else if(isMovable(CMD_LEFT)){
							move(CMD_LEFT);
						}else if(isMovable(CMD_DOWN)){
							move(CMD_DOWN);
						}else{
							printPanel();
							throw new Exception("Move�s�I�I lastcommand" + lastCommand);
						}
						moveBlankPanel(targetH,targetW);
						return;
					}
				}
			}
			//���ړ�
			if (moveHorizontal >0){
				for(int i=0;i<Math.abs(moveHorizontal);i++){
					//�ړ��\���`�F�b�N
					//�ړ��s�Ȃ炸��ĉ�蓹
					if(isMovable(CMD_RIGHT)){
						move(CMD_RIGHT);
					}else{
						//����čēx����
						if(isMovable(CMD_UP)){
							move(CMD_UP);
						}else if(isMovable(CMD_DOWN)){
							move(CMD_DOWN);
						}else if(isMovable(CMD_LEFT)){
							move(CMD_LEFT);
						}else{
							printPanel();
							throw new Exception("Move�s�I�I lastcommand" + lastCommand);
						}
						moveBlankPanel(targetH,targetW);
						return;
					}
				}
			}else{
				for(int i=0;i<Math.abs(moveHorizontal);i++){
					//�ړ��\���`�F�b�N
					//�ړ��s�Ȃ炸��ĉ�蓹
					if(isMovable(CMD_LEFT)){
						move(CMD_LEFT);
					}else{
						//����čēx����
						if(isMovable(CMD_UP)){
							move(CMD_UP);
						}else if(isMovable(CMD_DOWN)){
							move(CMD_DOWN);
						}else if(isMovable(CMD_RIGHT)){
							move(CMD_RIGHT);
						}else{
							printPanel();
							throw new Exception("Move�s�I�I lastcommand" + lastCommand);
						}
						moveBlankPanel(targetH,targetW);
						return;
					}
				}
			}
			
		}else{
			//�ŏI�R�}���h���c�̏ꍇ�͉��ړ�����
			
			//���ړ�
			if (moveHorizontal >0){
				for(int i=0;i<Math.abs(moveHorizontal);i++){
					//�ړ��\���`�F�b�N
					//�ړ��s�Ȃ炸��ĉ�蓹
					if(isMovable(CMD_RIGHT)){
						move(CMD_RIGHT);
					}else{
						//����čēx����
						if(isMovable(CMD_UP)){
							move(CMD_UP);
						}else if(isMovable(CMD_DOWN)){
							move(CMD_DOWN);
						}else if(isMovable(CMD_LEFT)){
							move(CMD_LEFT);
						}else{
							printPanel();
							throw new Exception("Move�s�I�I lastcommand" + lastCommand);
						}
						moveBlankPanel(targetH,targetW);
						return;
					}
				}
			}else{
				for(int i=0;i<Math.abs(moveHorizontal);i++){
					//�ړ��\���`�F�b�N
					//�ړ��s�Ȃ炸��ĉ�蓹
					if(isMovable(CMD_LEFT)){
						move(CMD_LEFT);
					}else{
						//����čēx����
						if(isMovable(CMD_UP)){
							move(CMD_UP);
						}else if(isMovable(CMD_DOWN)){
							move(CMD_DOWN);
						}else if(isMovable(CMD_RIGHT)){
							move(CMD_RIGHT);
						}else{
							printPanel();
							throw new Exception("Move�s�I�I lastcommand" + lastCommand);
						}
						moveBlankPanel(targetH,targetW);
						return;
					}
				}
			}
			//�c�ړ�
			if (moveVertical >0){
				for(int i=0;i<Math.abs(moveVertical);i++){
					//�ړ��\���`�F�b�N
					//�ړ��s�Ȃ炸��ĉ�蓹
					if(isMovable(CMD_DOWN)){
						move(CMD_DOWN);
					}else{
						//����čēx����
						if(isMovable(CMD_RIGHT)){
							move(CMD_RIGHT);
						}else if(isMovable(CMD_LEFT)){
							move(CMD_LEFT);
						}else if(isMovable(CMD_UP)){
							move(CMD_UP);
						}else{
							printPanel();
							throw new Exception("Move�s�I�I lastcommand" + lastCommand);
						}
						moveBlankPanel(targetH,targetW);
						return;
					}
				}
			}else{
				for(int i=0;i<Math.abs(moveVertical);i++){
					//�ړ��\���`�F�b�N
					//�ړ��s�Ȃ炸��ĉ�蓹
					if(isMovable(CMD_UP)){
						move(CMD_UP);
					}else{
						//����čēx����
						if(isMovable(CMD_RIGHT)){
							move(CMD_RIGHT);
						}else if(isMovable(CMD_LEFT)){
							move(CMD_LEFT);
						}else if(isMovable(CMD_DOWN)){
							move(CMD_DOWN);
						}else{
							printPanel();
							throw new Exception("Move�s�I�I lastcommand" + lastCommand);
						}
						moveBlankPanel(targetH,targetW);
						return;
					}
				}
			}
		}

	}
	
	/**
	 * �󔒂��w�蕶���̎w��אڏꏊ�Ɉړ�����R�X�g�v�Z
	 * @param targetVal�@�w�蕶��
	 * @param pos�@�w��אڏꏊ�@CMD_XX
	 * @return�@�R�X�g
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
		//�P���ʒu����R�X�g�Z�o
		cost = Math.abs(posW - targetW)+Math.abs(posH - targetH);
		//��蓹�R�X�g
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
	
	//�w��ʒu�̃p�l�����w��ʒu�Ɉړ�������o�b�`
	private void batchMove(int h,int w,int targetH,int targetW) throws Exception{
		batchMove(getPiece(h,w),targetH,targetW);
	}

	//�w��ʒu�̒l���擾
	private char getPiece(int h,int w){
		return panel[h*width+w];
	}
	/**
	 * ���̃p�l�����������ǂ����`�F�b�N
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
	 * �ړ��\���ǂ����̃X�e�[�^�X��Ԃ�
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
	 * �ړ��\���`�F�b�N
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
			//�O��ړ��Ƌt�R�}���h�̓_��
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
			//�O��ړ��Ƌt�R�}���h�̓_��
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
			//�O��ړ��Ƌt�R�}���h�̓_��
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
			//�O��ړ��Ƌt�R�}���h�̓_��
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
	
	// �ړ����W�b�N
	private void move(char command) throws Exception {
		char work;
		int moveH = -1;
		int moveW = -1;
		
		if (command == CMD_UP) {
			moveH = posH - 1;
			moveW = posW;
			// �p�l���̐������炷
			this.up--;
		} else if (command == CMD_DOWN) {
			moveH = posH + 1;
			moveW = posW;
			// �p�l���̐������炷
			this.down--;
		} else if (command == CMD_LEFT) {
			moveH = posH;
			moveW = posW - 1;
			// �p�l���̐������炷
			this.left--;
		} else if (command == CMD_RIGHT) {
			moveH = posH;
			moveW = posW + 1;
			// �p�l���̐������炷
			this.right--;
		}
		work = panel[posH * width + posW];
		panel[posH * width + posW] = panel[moveH * width + moveW];
		panel[moveH * width + moveW] = work;
		// ���݂̃|�W�V�����Z�b�g
		posH = moveH;
		posW = moveW;
		// �񓚒ǉ�
		answer.append(command);
		lastCommand = command;
	}

	// Rank�擾
	// ���Ə�͍ċA�I�ɌĂяo��
	private int getRank(char command, int h, int w, boolean isRecursive) {
		int rtn = 0;
		char val;
		// �Ǎ�
		if ((command == CMD_LEFT && w == 0)
				|| (command == CMD_RIGHT && w == width - 1)
				|| (command == CMD_UP && h == 0)
				|| (command == CMD_DOWN && h == height - 1)) {
			// �ċA�s��
			if (isRecursive) {
				// �ċA�Ăяo���̏ꍇ�͍l�����Ȃ�
				return 0;
			} else {
				return -100;
			}
		}
		// ����ւ��Ώ�pos
		// �O��Ƌt�Ɉړ����悤�Ƃ��Ă���ꍇ�̓}�C�i�X�����N
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

		// �ړ��Ώۂ��ǂȂ�ړ��s��
		if (val == '=') {
			// �ċA�s��
			if (isRecursive) {
				// �ċA�Ăяo���̏ꍇ�͍l�����Ȃ�
				return 0;
			} else {
				return -100;
			}
		}

		// ���Ə�͍ċA�ł��̍��Ə��Rank���Ώۂɂ���
		if (command == CMD_LEFT || command == CMD_UP) {
			rtn += getRank(CMD_LEFT, h, w, true) + getRank(CMD_UP, h, w, true);
		}

		// ���݈ʒu����̍������o��
		int rankH = Math.abs(h - getCorrectH(val));
		int rankW = Math.abs(w - getCorrectW(val));

		rtn += rankH + rankW;
		if (isRecursive) {
			rtn = 0;
		}
		return rtn;
	}

	/**
	 * �w�蕶���̌��ݍ�����Ԃ�
	 * @param val
	 * @return
	 */
	private int getNowHeightOf(char val){
		return String.valueOf(panel).indexOf(val) / width;
	}

	/**
	 * �w�蕶���̌��ݕ���Ԃ�
	 * @param val
	 * @return
	 */
	private int getNowWidthOf(char val){
		return String.valueOf(panel).indexOf(val) % width;
	}

	// �w��ꏊ�̂���ׂ��p�l����Ԃ�
	private char getCorrectVal(int h, int w) {
		return ANS_PANEL[h * width + w];
	}

	// �w�蕶���̐����̍�����Ԃ�
	private int getCorrectH(char val) {
		return String.valueOf(ANS_PANEL).indexOf(val) / width;

	}

	// �w�蕶���̐����̕���Ԃ�
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
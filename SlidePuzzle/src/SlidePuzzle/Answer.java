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
		//Question q = qList.get(3);
		Question q;
		for(int i=0;i<cntQuestion;i++){
			q= qList.get(i);
			q.solve(cntL, cntR, cntU, cntD);
			if (q.isCorrect()){
				// �������c��J�E���g�X�V
				cntL = q.getLeft();
				cntR = q.getRight();
				cntU = q.getUp();
				cntD = q.getDown();
			}
		}
		//No���\�[�g
		Collections.sort(qList, new NoSort());


		// �񓚏o�͕�
		try {
			/* FileWriter �N���X�̃C���X�^���X���쐬���܂��B */
			FileWriter fw = new FileWriter("./answer.txt");
			/* �t�@�C���ɏ������݂܂��B */
			for(int i=0;i<cntQuestion;i++){
				q = qList.get(i);
				if(q.isCorrect()){
					fw.write(q.getAnswer());
				}else{
					fw.write("");
				}
			}
			/* �t�@�C�����N���[�Y���܂��B */
			fw.close();
		} catch (IOException e) {
			System.out.println(e + "��O���������܂���");
		}
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
	private String[][] panel;
	//�����t���O
	private boolean correct;
	// �񓚃R�}���h
	private StringBuffer answer;

	// �Ō�Ɏ��s�����R�}���h
	private String lastCommand;

	// �c��ړ���
	private int left;
	private int right;
	private int up;
	private int down;

	private static final String ANS_PANEL = "123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public Question(int no, int height, int width, String data) {
		this.setNo(no);
		this.height = height;
		this.width = width;
		// �p�l���̍���*�������x���Ƃ���
		this.setLevel(height * width);
		//�s�������
		this.correct =false;
		// �p�l���쐬
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
		// �񓚏�����
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

	// �𓚃��W�b�N
	public boolean solve(int left, int right, int up, int down) {
		// �c�p�l���Z�b�g
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
				if (opCnt > 100) {
					System.out.println("MAX OPERATION!!");
					break;
				}
				int goUp, goDown, goLeft, goRight;
				if (lastCommand.equals("D")) {
					goUp = 0;
				} else {
					goUp = getUpRank();
				}
				if (lastCommand.equals("U")) {
					goDown = 0;
				} else {
					goDown = getDownRank();
				}
				if (lastCommand.equals("R")) {
					goLeft = 0;
				} else {
					goLeft = getLeftRank();
				}
				if (lastCommand.equals("L")) {
					goRight = 0;
				} else {
					goRight = getRightRank();
				}
				if (goUp <= 0 && goDown <= 0 && goLeft <= 0 && goRight <= 0 && posH == height-1 && posW == width-1) {
					System.out.println("end operation");
					//�����`�F�b�N
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

	//���̃p�l�����������ǂ����`�F�b�N
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
	
	// �ړ����W�b�N
	private void Up() throws Exception {
		String work;
		if (posH == 0) {
			throw new Exception("Can't Up! H=" + posH + "posW=" + posW);
		}
		if (panel[posH - 1][posW].equals("=")) {

		}
		work = panel[posH][posW];
		panel[posH][posW] = panel[posH - 1][posW];
		panel[posH - 1][posW] = work;
		// ���݂̃|�W�V�����Z�b�g
		posH--;
		// �p�l���̐������炷
		this.up--;
		// �񓚒ǉ�
		answer.append("U");
		lastCommand = "U";
	}

	private void Down() throws Exception {
		String work;
		if (posH == height - 1) {
			throw new Exception("Can't Down! H=" + posH + "posW=" + posW);
		}
		work = panel[posH][posW];
		panel[posH][posW] = panel[posH + 1][posW];
		panel[posH + 1][posW] = work;
		// ���݂̃|�W�V�����Z�b�g
		posH++;
		// �p�l���̐������炷
		this.down--;
		// �񓚒ǉ�
		answer.append("D");
		lastCommand = "D";
	}

	private void Left() throws Exception {
		String work;
		if (posW == 0) {
			throw new Exception("Can't Left! H=" + posH + "posW=" + posW);
		}
		work = panel[posH][posW];
		panel[posH][posW] = panel[posH][posW - 1];
		panel[posH][posW - 1] = work;
		// ���݂̃|�W�V�����Z�b�g
		posW--;
		// �p�l���̐������炷
		this.left--;
		// �񓚒ǉ�
		answer.append("L");
		lastCommand = "L";

	}

	private void Right() throws Exception {
		String work;
		if (posW == width - 1) {
			throw new Exception("Can't Right! H=" + posH + "posW=" + posW);
		}
		work = panel[posH][posW];
		panel[posH][posW] = panel[posH][posW + 1];
		panel[posH][posW + 1] = work;
		// ���݂̃|�W�V�����Z�b�g
		posW++;
		// �p�l���̐������炷
		this.right--;
		// �񓚒ǉ�
		answer.append("R");
		lastCommand = "R";
	}

	// ��̃p�l����Rank��Ԃ��A�ړ��ł��Ȃ��ꍇ��-1 �A�������p�l���ʒu�̏ꍇ��0��Ԃ�
	private int getUpRank() {
		if (posH == 0) {
			return -1;
		}
		String val = panel[posH - 1][posW];
		if (val.equals("=")) {
			return -1;
		}

		if (val.equals(getCorrectVal(posH - 1, posW))) {
			return 0;

		}
		int rankH = (posH - 1) - getCorrectH(val);
		int rankW = posW - getCorrectW(val);
		// System.out.println(rankH + " " + rankW);
		return Math.abs(rankH) + Math.abs(rankW);

	}

	// ���̃p�l����Rank��Ԃ��A�ړ��ł��Ȃ��ꍇ��-1 �A�������p�l���ʒu�̏ꍇ��0��Ԃ�
	private int getDownRank() {
		if (posH == height - 1) {
			return -1;
		}
		String val = panel[posH + 1][posW];
		if (val.equals("=")) {
			return -1;
		}

		if (val.equals(getCorrectVal(posH + 1, posW))) {
			return 0;

		}
		int rankH = (posH + 1) - getCorrectH(val);
		int rankW = posW - getCorrectW(val);
		// System.out.println(rankH + " " + rankW);
		return Math.abs(rankH) + Math.abs(rankW);

	}

	// ���̃p�l����Rank��Ԃ��A�ړ��ł��Ȃ��ꍇ��-1 �A�������p�l���ʒu�̏ꍇ��0��Ԃ�
	private int getLeftRank() {
		if (posW == 0) {
			return -1;
		}
		String val = panel[posH][posW - 1];
		if (val.equals("=")) {
			return -1;
		}

		if (val.equals(getCorrectVal(posH, posW - 1))) {
			return 0;

		}
		int rankH = (posH) - getCorrectH(val);
		int rankW = posW - 1 - getCorrectW(val);
		// System.out.println(rankH + " " + rankW);
		return Math.abs(rankH) + Math.abs(rankW);

	}

	// �E�̃p�l����Rank��Ԃ��A�ړ��ł��Ȃ��ꍇ��-1 �A�������p�l���ʒu�̏ꍇ��0��Ԃ�
	private int getRightRank() {
		if (posW == height - 1) {
			return -1;
		}
		String val = panel[posH][posW + 1];
		if (val.equals("=")) {
			return -1;
		}

		if (val.equals(getCorrectVal(posH, posW + 1))) {
			return 0;

		}
		int rankH = (posH) - getCorrectH(val);
		int rankW = posW + 1 - getCorrectW(val);
		// System.out.println(rankH + " " + rankW);
		return Math.abs(rankH) + Math.abs(rankW);

	}

	// �w��ꏊ�̂���ׂ��p�l����Ԃ�
	private String getCorrectVal(int H, int W) {
		return String.valueOf(ANS_PANEL.charAt(H * height + W));
	}

	// �w�蕶���̐����̍�����Ԃ�
	private int getCorrectH(String val) {
		return ANS_PANEL.indexOf(val) / width;

	}

	// �w�蕶���̐����̕���Ԃ�
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
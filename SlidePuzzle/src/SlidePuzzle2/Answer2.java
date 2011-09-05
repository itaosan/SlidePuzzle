package SlidePuzzle2;

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

public class Answer2 {

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
		// Question q = qList.get(3);
		// q.solve(cntL, cntR, cntU, cntD);

		Question q;
		for (int i = 0; i < cntQuestion; i++) {
			q = qList.get(i);
			q.solve(cntL, cntR, cntU, cntD);
			if (q.isCorrect()) {
				// �������c��J�E���g�X�V
				cntL = q.getLeft();
				cntR = q.getRight();
				cntU = q.getUp();
				cntD = q.getDown();
			}
		}
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
		int goUp = 0;
		int goDown = 0;
		int goLeft = 0;
		int goRight = 0;
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
				goUp = getRank(CMD_UP, posH, posW, false);
				goDown = getRank(CMD_DOWN, posH, posW, false);
				goLeft = getRank(CMD_LEFT, posH, posW, false);
				goRight = getRank(CMD_RIGHT, posH, posW, false);
				if (goUp <= 0 && goDown <= 0 && goLeft <= 0 && goRight <= 0
						&& posH == height - 1 && posW == width - 1) {
					System.out.println("end operation");
					// �����`�F�b�N
					if (chk()) {
						correct = true;
					}
					break;
				}
				int[] list = { goUp, goLeft, goDown, goRight };
				Arrays.sort(list);
				int val = list[list.length - 1];
				if (val == goUp) {
					move(CMD_UP);
				} else if (val == goLeft) {
					move(CMD_LEFT);
				} else if (val == goDown) {
					move(CMD_DOWN);
				} else if (val == goRight) {
					move(CMD_RIGHT);
				} else {
					throw new Exception("No move!?!?!");
				}

			}
			printPanel();
			System.out.println(getAnswer());
//			System.out.println("up" + goUp);
//			System.out.println("down" + goDown);
//			System.out.println("left" + goLeft);
//			System.out.println("right" + goRight);

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	// ���̃p�l�����������ǂ����`�F�b�N
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

	// �ړ����W�b�N
	private void move(char command) throws Exception {
		char work;
		int moveH = -1;
		int moveW = -1;
		if (command == CMD_UP) {
			if (posH == 0) {
				throw new Exception("Can't Up! H=" + posH + "posW=" + posW);
			}
			moveH = posH - 1;
			moveW = posW;
			// �p�l���̐������炷
			this.up--;
		} else if (command == CMD_DOWN) {
			if (posH == height - 1) {
				throw new Exception("Can't Down! H=" + posH + "posW=" + posW);
			}
			moveH = posH + 1;
			moveW = posW;
			// �p�l���̐������炷
			this.down--;
		} else if (command == CMD_LEFT) {
			if (posW == 0) {
				throw new Exception("Can't Left! H=" + posH + "posW=" + posW);
			}
			moveH = posH;
			moveW = posW - 1;
			// �p�l���̐������炷
			this.left--;
		} else if (command == CMD_RIGHT) {
			if (posW == width - 1) {
				throw new Exception("Can't Right! H=" + posH + "posW=" + posW);
			}
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
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
					qes.printPanel();

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

		// ���𓚃��W�b�N

		// �񓚏o�͕�
		try {
			/* FileWriter �N���X�̃C���X�^���X���쐬���܂��B */
			FileWriter fw = new FileWriter("./answer.txt");
			/* �t�@�C���ɏ������݂܂��B */
			fw.write("write text !");
			/* �t�@�C�����N���[�Y���܂��B */
			fw.close();
		} catch (IOException e) {
			System.out.println(e + "��O���������܂���");
		}
	}

}

// ���N���X
class Question {
	// ���No
	private int no;
	// ���level
	private int level;

	// �p�l������
	private int height;
	// �p�l����
	private int width;
	// ���z��
	// ���f�[�^
	private String data;
	// �p�l��
	private String[][] panel;

	public Question(int no, int height, int width, String data) {
		this.setNo(no);
		this.height = height;
		this.width = width;
		// �p�l���̍���*�������x���Ƃ���
		this.setLevel(height * width);
		//�p�l���쐬
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
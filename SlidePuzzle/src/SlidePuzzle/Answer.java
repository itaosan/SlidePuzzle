package SlidePuzzle;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Answer {

	/**
	 * hogeee
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Question a = new Question(3, 3, "aiueo");
		System.out.println(a.getData());
		try {
			/* �t�@�C�����I�[�v�����܂��B */
			BufferedReader br = new BufferedReader(new FileReader(
					"problems.txt"));

			/* �t�@�C����ǂݍ��݂܂��B */
			String line = "";
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
			/* �t�@�C�����N���[�Y���܂��B */
			br.close();
		} catch (IOException e) {
			System.out.println(e + "��O���������܂���");
		}

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

class Question {
	private int row;
	private int col;
	private String data;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Question(int row, int col, String data) {
		this.row = row;
		this.col = col;
		this.data = data;
	}
}
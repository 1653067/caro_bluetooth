/**
  * @author		ThoaiDP
  * @copyright	2018 ThoaiDP
  * @version	1.0
  */
package com.android16_team.caro_project;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AlphaBeta {

	private int[][] board;

	private String[] caseHuman = { "0110", "01112", "0110102", "21110", "010110", "011010", "01110", "011112", "211110","2111010", "011110", "11111", "0111012", "10101011", "0101110", "0111010", "0111102", "110110", "011011","0101112", "11110", ";11110", "01111;" };
	private String[] caseAI =    { "0220", "02221", "0220201", "12220", "020220", "022020", "02220", "022221", "122220","1222020", "022220", "22222", "0222021", "20202022", "0202220", "0222020", "0222201", "220220", "022022","0202221", "22220", ";22220", "02222;" };
	private int[] point =        {  6,      4,        4,         4,       12,        12,      12,     1000,    1000,    1000,      3000,    10000,    1000,      3000,       1000,        1000,      1000,    1000,	  1000,    1000,     1000,      1000,   1000 };

	// Điểm cho tấn công và phòng thủ
	private int[] defenseScore = { 0, 1, 9, 85, 769 };
	private int[] attackScore = { 0, 4, 28, 256, 2308 };

	// Giới hạn chiều sâu của thuật toán
	private int maxDepth;

	// Giới hạn chọn bước đi
	private int maxNode;

	// Biến tạm kích thước bàn cờ
	private int size;

	// Điểm cho từng Node
	private int[][] evaluteNode;

	Random ran;

	public AlphaBeta(int size, int maxDepth) {
		this.size = size;
		ran = new Random();
		evaluteNode = new int[size][size];
		maxNode = 4;
		this.maxDepth = maxDepth;
	}

	public void setBoard(int[][] board) {
		this.board = board;
	}

	public int max(int a, int b) {
		return a > b ? a : b;
	}

	public int min(int a, int b) {
		return a < b ? a : b;
	}

	//dem so chuoi con trong s phu hop voi find
	public int count(String s, String find) {
		Pattern pattern = Pattern.compile(find);
		Matcher matcher = pattern.matcher(s);
		int i = 0;
		while (matcher.find()) {
			i++;
		}
		return i;
	}

	// đánh giá điểm cho bàn cờ
	public int evaluateBoard() {
		StringBuilder sb = new StringBuilder();

		// dọc
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				sb.append(board[j][i]);
			}
			sb.append(";");
		}

		// ngang
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				sb.append(board[i][j]);
			}
			sb.append(";");
		}

		int n = this.size * 2 - 1, x, y;

		// chéo phải (/)
		for (int i = 0; i < n; i++) {
			if (i >= size) {
				x = size - 1;
				y = i - size + 1;
			} else {
				x = i;
				y = 0;
			}
			for (; x > -1 && y < size; x--, y++) {
				sb.append(this.board[x][y]);
			}
			sb.append(";");
		}

		// chéo trái (\)
		for (int i = 0; i < n; i++) {
			if (size - i <= 0) {
				x = 0;
				y = i - size + 1;
			} else {
				x = size - i - 1;
				y = 0;
			}
			for (; x < size && y < size; x++, y++) {
				sb.append(board[x][y]);
			}
			sb.append(";");
		}
		// X = 1 human, O = 2 AI;
		String key1, key2;
		int score = 0;
		for (int i = 0; i < caseHuman.length; i++) {
			key1 = caseAI[i];
			key2 = caseHuman[i];
			score += point[i] * count(sb + "", key1);
			score -= point[i] * count(sb + "", key2);
		}
		return score;
	}

	// Làm mới điểm các node
	private void resetValue() {
		for (int i = 0; i < this.size; i++) {
			for (int j = 0; j < this.size; j++) {
				this.evaluteNode[i][j] = 0;
			}
		}
	}

	public void evaluateEachNode(int Player) {
		this.resetValue();
		int row, colum, i;
		int countAI, countHuman;
		/**
		 * Hàng
		 */
		for (row = 0; row < this.size; row++) {
			for (colum = 0; colum < this.size - 4; colum++) {
				countAI = 0;
				countHuman = 0;
				for (i = 0; i < 5; i++) {
					if (this.board[row][colum + i] == 2) {
						countAI++;
					}
					if (this.board[row][colum + i] == 1) {
						countHuman++;
					}
				}
				if (countAI * countHuman == 0 && countAI != countHuman) {
					for (i = 0; i < 5; i++) {
						if (this.board[row][colum + i] == 0) {
							if (countAI == 0) {
								if (Player == 2)
								{
									this.evaluteNode[row][colum + i] += defenseScore[countHuman];
								} else {
									this.evaluteNode[row][colum + i] += attackScore[countHuman];
								}
								if (checkValidNode(row, colum - 1) && checkValidNode(row, colum + 5)
										&& this.board[row][colum - 1] == 2 && this.board[row][colum + 5] == 2) {
									this.evaluteNode[row][colum + i] = 0;
								}
							}
							if (countHuman == 0) {
								if (Player == 1) {
									this.evaluteNode[row][colum + i] += defenseScore[countAI];
								} else {
									this.evaluteNode[row][colum + i] += attackScore[countAI];
								}
								if (checkValidNode(row, colum - 1) && checkValidNode(row, colum + 5)
										&& this.board[row][colum - 1] == 1 && this.board[row][colum + 5] == 1) {
									this.evaluteNode[row][colum + i] = 0;
								}
							}
							if ((countAI == 4 || countHuman == 4) &&
									((checkValidNode(row, colum + i - 1)&& this.board[row][colum + i - 1] == 0)
											||(checkValidNode(row, colum + i + 1) && this.board[row][colum + i + 1] == 0))) {
								this.evaluteNode[row][colum + i] *= 2;
							} else if (countAI == 4 || countHuman == 4) {
								this.evaluteNode[row][colum + i] *= 2;
							}
						}
					}
				}
			}
		}
		/**
		 * Cột
		 */
		for (row = 0; row < this.size - 4; row++) {
			for (colum = 0; colum < this.size; colum++) {
				countAI = 0;
				countHuman = 0;
				for (i = 0; i < 5; i++) {
					if (this.board[row + i][colum] == 2) {
						countAI++;
					}
					if (this.board[row + i][colum] == 1) {
						countHuman++;
					}
				}
				if (countAI * countHuman == 0 && countAI != countHuman) {
					for (i = 0; i < 5; i++) {
						if (this.board[row + i][colum] == 0) {
							if (countAI == 0) {
								if (Player == 2) {
									this.evaluteNode[row + i][colum] += defenseScore[countHuman];
								} else {
									this.evaluteNode[row + i][colum] += attackScore[countHuman];
								}
								if (checkValidNode(row - 1, colum) && checkValidNode(row + 5, colum)
										&& this.board[row - 1][colum] == 2 && this.board[row + 5][colum] == 2) {
									this.evaluteNode[row + i][colum] = 0;
								}
							}
							if (countHuman == 0) {
								if (Player == 1) {
									this.evaluteNode[row + i][colum] += defenseScore[countAI];
								} else {
									this.evaluteNode[row + i][colum] += attackScore[countAI];
								}
								if (checkValidNode(row - 1, colum) && checkValidNode(row + 5, colum)
										&& this.board[row - 1][colum] == 1 && this.board[row + 5][colum] == 1) {
									this.evaluteNode[row + i][colum] = 0;
								}
							}
							if ((countAI == 4 || countHuman == 4) && ((checkValidNode(row + i - 1, colum)
									&& this.board[row + i - 1][colum] == 0)
									|| (checkValidNode(row + i + 1, colum) && this.board[row + i + 1][colum] == 0))) {
								this.evaluteNode[row + i][colum] *= 2;
							} else if (countAI == 4 || countHuman == 4) {
								this.evaluteNode[row + i][colum] *= 2;
							}
						}
					}
				}
			}
		}
		/**
		 * Chéo
		 */
		for (row = 0; row < this.size - 4; row++) {
			for (colum = 0; colum < this.size - 4; colum++) {
				countAI = 0;
				countHuman = 0;
				for (i = 0; i < 5; i++) {
					if (this.board[row + i][colum + i] == 2) {
						countAI++;
					}
					if (this.board[row + i][colum + i] == 1) {
						countHuman++;
					}
				}
				if (countAI * countHuman == 0 && countAI != countHuman) {
					for (i = 0; i < 5; i++) {
						if (this.board[row + i][colum + i] == 0) {
							if (countAI == 0) {
								if (Player == 2) {
									this.evaluteNode[row + i][colum + i] += defenseScore[countHuman];
								} else {
									this.evaluteNode[row + i][colum + i] += attackScore[countHuman];
								}
								if (checkValidNode(row - 1, colum - 1) && checkValidNode(row + 5, colum + 5)
										&& this.board[row - 1][colum - 1] == 2 && this.board[row + 5][colum + 5] == 2) {
									this.evaluteNode[row + i][colum + i] = 0;
								}
							}
							if (countHuman == 0) {
								if (Player == 1) {
									this.evaluteNode[row + i][colum + i] += defenseScore[countAI];
								} else {
									this.evaluteNode[row + i][colum + i] += attackScore[countAI];
								}
								if (checkValidNode(row - 1, colum - 1) && checkValidNode(row + 5, colum + 5)
										&& this.board[row - 1][colum - 1] == 1 && this.board[row + 5][colum + 5] == 1) {
									this.evaluteNode[row + i][colum + i] = 0;
								}
							}
							if ((countAI == 4 || countHuman == 4) && ((checkValidNode(row + i - 1, colum + i - 1)
									&& this.board[row + i - 1][colum + i - 1] == 0)
									|| (checkValidNode(row + i + 1, colum + i + 1)
											&& this.board[row + i + 1][colum + i + 1] == 0))) {
								this.evaluteNode[row + i][colum + i] *= 2;
							} else if (countAI == 4 || countHuman == 4) {
								this.evaluteNode[row + i][colum + i] *= 2;
							}
						}
					}
				}
			}
		}
		/**
		 * Chéo
		 */
		for (row = 4; row < this.size; row++) {
			for (colum = 0; colum < this.size - 4; colum++) {
				countAI = 0;
				countHuman = 0;
				for (i = 0; i < 5; i++) {
					if (this.board[row - i][colum + i] == 2) {
						countAI++;
					}
					if (this.board[row - i][colum + i] == 1) {
						countHuman++;
					}
				}
				if (countAI * countHuman == 0 && countAI != countHuman) {
					for (i = 0; i < 5; i++) {
						if (this.board[row - i][colum + i] == 0) {
							if (countAI == 0) {
								if (Player == 2) {
									this.evaluteNode[row - i][colum + i] += defenseScore[countHuman];
								} else {
									this.evaluteNode[row - i][colum + i] += attackScore[countHuman];
								}
								if (checkValidNode(row + 1, colum - 1) && checkValidNode(row - 5, colum + 5)
										&& this.board[row + 1][colum - 1] == 2 && this.board[row - 5][colum + 5] == 2) {
									this.evaluteNode[row - i][colum + i] = 0;
								}
							}
							if (countHuman == 0) {
								if (Player == 1) {
									this.evaluteNode[row - i][colum + i] += defenseScore[countAI];
								} else {
									this.evaluteNode[row - i][colum + i] += attackScore[countAI];
								}
								if (checkValidNode(row + 1, colum - 1) && checkValidNode(row - 5, colum + 5)
										&& this.board[row + 1][colum - 1] == 1 && this.board[row - 5][colum + 5] == 1) {
									this.evaluteNode[row + i][colum + i] = 0;
								}
							}
							if ((countAI == 4 || countHuman == 4) && ((checkValidNode(row - i + 1, colum + i - 1)
									&& this.board[row - i + 1][colum + i - 1] == 0)
									|| (checkValidNode(row - i - 1, colum + i + 1)
											&& this.board[row - i - 1][colum + i + 1] == 0))) {
								this.evaluteNode[row - i][colum + i] *= 2;
							} else if (countAI == 4 || countHuman == 4) {
								this.evaluteNode[row - i][colum + i] *= 2;
							}
						}
					}
				}
			}
		}
	}

	// Kiểm tra hợp lệ Node
	public boolean checkValidNode(int x, int y) {
		return x > -1 && y > -1 && x < this.size && y < this.size;
	}

	// Trả về các Node tiếp theo có điểm cao nhất ( số lượng maxNode)
	public List<Node> getNextNode() {
		List<Node> tmp = new ArrayList<>(), buff;
		int max, random;
		for (int k = 0; k < this.maxNode; k++) {
			max = Integer.MIN_VALUE;
			buff = new ArrayList<>();
			for (int i = 0; i < this.size; i++) {
				for (int j = 0; j < this.size; j++) {
					if (board[i][j] == 0) {
						if (max < this.evaluteNode[i][j]) {
							max = this.evaluteNode[i][j];
							buff.clear();
							buff.add(new Node(i, j, max));
						} else if (max == this.evaluteNode[i][j]) {
							buff.add(new Node(i, j, max));
						}
					}
				}
			}

			random = this.ran.nextInt(buff.size());
			tmp.add(buff.get(random));
			this.evaluteNode[buff.get(random).getX()][buff.get(random).getY()] = Integer.MIN_VALUE;
		}
		return tmp;
	}

	// Trả về bước đi tiếp theo tốt nhất
	public Node getBestNode() {
		evaluateEachNode(2);
		List<Node> list = this.getNextNode();
		int index = -1, max = Integer.MIN_VALUE, val;
		Node n;
		for (int i = 0; i < list.size(); i++) {
			n = list.get(i);
			// c
			this.board[n.getX()][n.getY()] = 2;
			val = alphaBeta(Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 1);
			System.out.println(n.getX() + "-" + n.getY() + ": " + val);
			// r
			this.board[n.getX()][n.getY()] = 0;

			if (max < val) {
				max = val;
				index = i;
			}
		}
		return list.get(index);
	}

	// thuật toán cắt tỉa alpha-beta
	public int alphaBeta(int a, int b, int depth, int turn) {

		int val = evaluateBoard(), rs;
		if (depth >= maxDepth || Math.abs(val) > 3000) {
			return val;
		}
		evaluateEachNode(turn);
		List<Node> list = this.getNextNode();
		if (turn == 1) {
			rs = Integer.MAX_VALUE;
			for (Node n : list) {
				// c
				this.board[n.getX()][n.getY()] = turn;

				rs = min(rs, alphaBeta(a, b, depth + 1, 2));
				b = min(b, rs);

				// r
				this.board[n.getX()][n.getY()] = 0;

				if (b <= a) {
					break;
				}
			}
			return rs;
		} else {
			rs = Integer.MIN_VALUE;
			for (Node n : list) {
				// c
				this.board[n.getX()][n.getY()] = turn;

				rs = max(rs, alphaBeta(a, b, depth + 1, 1));
				a = max(a, rs);

				// r
				this.board[n.getX()][n.getY()] = 0;

				if (b <= a) {
					break;
				}
			}
			return rs;
		}
	}

	// kiểm tra bàn cờ đã hết chỗ đánh hay chưa
	public boolean checkNull() {
		for (int[] i : this.board) {
			for (int jj : i) {
				if (jj == 0) {
					return false;
				}
			}
		}
		return true;
	}

	public int[][] getEvaluteNode() {
		return evaluteNode;
	}

}

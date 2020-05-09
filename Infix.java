/**
* Converts a given infix expression into a postfix expression. 
* A project for my data structures class
*
* @author mullins-dev
*/

import java.util.Stack;

public class Infix {

	String infix;

	/**
	 * Constructs a new Infix object from a given infix expression.
	 * Only valid operators: + - * ^ /
	 * Types of parenthesis: [] or ()
	 * Also: cleans the expression so it is easy to push and pop from a stack
	 * @param the given infix expression
	 */
	public Infix(String in) throws Exception {
		this.infix = in;
		this.clean();
		if (parenBalanced() == false) {
			throw new Exception("Expression given has invalid parenthesis.");
		}
	}

	/**
	 * Returns the infix expression
	 * @return the infix expression
	 */
	public String toString() {
		return this.infix;
	}

	/**
	 * Breaks down the given expression to determine if it has balanced parenthesis
	 * @return true if the infix expression has balanced parenthesis, false otherwise
	 */
	private boolean parenBalanced() {
		String expression = this.infix;
		Stack<Character> expChecker = new Stack<Character>();
		for (int i = 0; i < expression.length(); i++ ) {
			// Check for leading parenthesis, pushes if is leading
			if (expression.charAt(i) == '(' || expression.charAt(i) == '[') {
				expChecker.push(expression.charAt(i));
			}
			// Checks for ending parenthesis
			if (expression.charAt(i) == ')' || expression.charAt(i) == ']') {
				// If the stack is empty, there must be no elements to match.. so return false
				if (expChecker.isEmpty() == true) {
					return false;
					// If they don't match, return false
				} else if (isMatchingPair(expChecker.pop(), expression.charAt(i)) == false) {
					return false;
				}
			}
		}
		// If the checker is empty, that means all elements have been popped and matched
		// or the expression just had no parenthesis, making the expression valid.
		if (expChecker.isEmpty() == true) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Helper method determining if a leading and a closing parenthesis match with one
	 * another.
	 * @return true if match, false otherwise
	 */
	private boolean isMatchingPair(char c1, char c2) {
		if (c1 == '(' && c2 == ')') {
			return true;
		} else if (c1 == '[' && c2 == ']') {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Converts the infix expression to a postfix expression.
	 * i.e: "3+4" -> "3 4 +"
	 * @return the trimmed, space-delimited postfix expression
	 */
	public String toPostFix() {
		String temp = this.infix;
		String postFix = "";
		String[] split = temp.split(" ");
		Stack<String> operations = new Stack<String>();
		int i = 0;
		while (i < split.length) {
			if (isNumber(split[i])) {
				postFix += split[i] + " ";
			} else if (isOpenParenthesis(split[i])) {
				operations.push(split[i]);
			} else if (isCloseParenthesis(split[i])) {
				while (operations.isEmpty() == false && !(isOpenParenthesis(operations.peek()))) {
					postFix += operations.pop() + " ";
				}
				operations.pop();
			} else if (isOperator(split[i])) {
				if (operations.isEmpty() == false && split[i].equals("^") == false) {
					while (operations.isEmpty() == false && precedenceChecker(operations.peek()) >= precedenceChecker(split[i])) {
						postFix += operations.pop() + " ";
					}
					operations.push(split[i]);
				} else {
					operations.push(split[i]);
				}
			}
			i++;
		}
		while (operations.isEmpty() == false) {
			postFix += operations.pop() + " ";
		}
		// No leading or trailing spaces!
		postFix.trim();
		return postFix;
	}

	/**
	 * Checks the precedence of an operator
	 * @return an int with regards to precendence ranking (1 being highest precedence.)
	 */
	private int precedenceChecker(String str) {
		if (str.equals("^")) {
			return 4;
		} else if (str.equals("*") || str.equals("/")) {
			return 3;
		} else if (str.equals("+") || str.equals("-")) {
			return 2;
		} else {
			return 0;
		}
	}

	/**
	 * Determines if the given string is an operator or not
	 * @return true, if the string is an operator, false otherwise
	 */
	private boolean isOperator(String str) {
		if (str.equals("+") || str.equals("-") || str.equals("*") || str.equals("/") || str.equals("^")) {
			return true;
		}
		return false;
	}

	/**
	 * Detemines if the given string is an opening parenthesis or not
	 * @return true, if the string is an opening parenthesis, false otherwise
	 */
	private boolean isOpenParenthesis(String str) {
		if (str.equals("(") || str.equals("[")) {
			return true;
		}
		return false;
	}

	/**
	 * Detemines if the given string is a closing parenthesis or not
	 * @return true, if the string is a closing parenthesis, false otherwise
	 */
	private boolean isCloseParenthesis(String str) {
		if (str.equals(")") || str.equals("]")) {
			return true;
		}
		return false;
	}

	/**
	 * Cleans a given infix expression
	 */
	private void clean() {
		char[] ops = "+-*/()[]^".toCharArray();
		for (char c : ops) {
			infix = infix.replace("" + c, " " + c + " ");
		}
		infix = infix.replaceAll("\\s+"," "); // replace all white spaces with a single space
		infix = infix.trim();
	}

	/**
	 * Determines whether a string is a number or not
	 * @param str the given string
	 * @return true if the given string is in fact a number, false otherwise
	 */
	private boolean isNumber(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch(NumberFormatException e){
			return false;
		}
	}

	/**
	 * Converts the given expression into a sum. The use of this method
	 * is to parse a string into a mathematical equation and do the operation
	 * @param i the first number
	 * @param t the second number
	 * @param s the expression
	 * @return
	 */
	private int converter(int i, int t, String s) {
		if (s.equals("^")) {
			return (int) Math.pow(i, t);
		} else if (s.equals("*")) {
			return i * t;
		} else if (s.equals("/")) {
			return i / t;
		} else if (s.equals("+")) {
			return i + t;
		} else if (s.equals("-")) {
			return i - t;
		} else {
			return 0;
		}
	}

	/**
	 * Computes the result of a given postfix expressio
	 * @return the result
	 */
	public int compute() {
		String[] split = this.toPostFix().split(" ");
		Stack<String> operators = new Stack<String>();
		int i = 0;
		String result = "";
		while (i < split.length) {
			if (isNumber(split[i])) {
				operators.push(split[i]);
			} else if (operators.isEmpty() == false) {
				int num2 = Integer.parseInt(operators.pop());
				int num1 = Integer.parseInt(operators.pop());
				result += converter(num1, num2, split[i]);
				operators.push(result);
				result = "";
			}
			i++;
		}
		int finResult = 0;
		while (operators.isEmpty() == false) {
			finResult += Integer.parseInt(operators.pop());
		}
		return finResult;
	}
}

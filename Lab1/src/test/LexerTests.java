package test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;

import lexer.Lexer;

import org.junit.Test;

import frontend.Token;
import frontend.Token.Type;
import static frontend.Token.Type.*;

/**
 * This class contains unit tests for your lexer. Currently, there is only one
 * test, but you
 * are strongly encouraged to write your own tests.
 */
public class LexerTests {
	// helper method to run tests; no need to change this
	private final void runtest(String input, Token... output) {
		Lexer lexer = new Lexer(new StringReader(input));
		int i = 0;
		Token actual = new Token(MODULE, 0, 0, ""), expected;
		try {
			do {
				assertTrue(i < output.length);
				expected = output[i++];
				try {
					actual = lexer.nextToken();
					assertEquals(expected, actual);
				} catch (Error e) {
					if (expected != null)
						fail(e.getMessage());
					/* return; */
				}
			} while (!actual.isEOF());
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/** Example unit test. */
	@Test
	public void testKWs() {
		// first argument to runtest is the string to lex; the remaining arguments
		// are the expected tokens
		runtest("module false return while true type void",
				new Token(MODULE, 0, 0, "module"),
				new Token(FALSE, 0, 7, "false"),
				new Token(RETURN, 0, 13, "return"),
				new Token(WHILE, 0, 20, "while"),
				new Token(TRUE, 0, 26, "true"),
				new Token(TYPE, 0, 31, "type"),
				new Token(VOID, 0, 36, "void"),
				new Token(EOF, 0, 40, ""));
	}

	@Test
	public void testPunc() {
		// first argument to runtest is the string to lex; the remaining arguments
		// are the expected tokens
		runtest("; [ { ) ] ,()",
				new Token(SEMICOLON, 0, 0, ";"),
				new Token(LBRACKET, 0, 2, "["),
				new Token(LCURLY, 0, 4, "{"),
				new Token(RPAREN, 0, 6, ")"),
				new Token(RBRACKET, 0, 8, "]"),
				new Token(COMMA, 0, 10, ","),
				new Token(LPAREN, 0, 11, "("),
				new Token(RPAREN, 0, 12, ")"),
				new Token(EOF, 0, 13, ""));
	}

	@Test
	public void testOperator() {
		// first argument to runtest is the string to lex; the remaining arguments
		// are the expected tokens
		runtest("== * >= / \n>==",
				new Token(EQEQ, 0, 0, "=="),
				new Token(TIMES, 0, 3, "*"),
				new Token(GEQ, 0, 5, ">="),
				new Token(DIV, 0, 8, "/"),
				new Token(GEQ, 1, 0, ">="),
				new Token(EQL, 1, 2, "="),
				new Token(EOF, 1, 3, ""));
	}

	@Test
	public void testIdentifier() {
		// first argument to runtest is the string to lex; the remaining arguments
		// are the expected tokens
		runtest("abc fgh a",
				new Token(ID, 0, 0, "abc"),
				new Token(ID, 0, 4, "fgh"),
				new Token(ID, 0, 8, "a"),
				new Token(EOF, 0, 9, ""));
	}

	@Test
	public void testPositioningAndLiteral() {
		// first argument to runtest is the string to lex; the remaining arguments
		// are the expected tokens
		runtest("\"abc\" \n1",
				new Token(STRING_LITERAL, 0, 0, "abc"),
				new Token(INT_LITERAL, 1, 0, "1"),
				new Token(EOF, 1, 1, ""));
	}

	@Test
	public void testAll() {
		// first argument to runtest is the string to lex; the remaining arguments
		// are the expected tokens
		runtest("> \"bcd\" , break",
				new Token(GT, 0, 0, ">"),
				new Token(STRING_LITERAL, 0, 2, "bcd"),
				new Token(COMMA, 0, 8, ","),
				new Token(BREAK, 0, 10, "break"),
				new Token(EOF, 0, 15, ""));
	}

	@Test
	public void testStringLiteralWithDoubleQuote() {
		runtest("\"\"\"",
				new Token(STRING_LITERAL, 0, 0, ""),
				(Token) null,
				new Token(EOF, 0, 3, ""));
	}

	@Test
	public void testStringLiteral() {
		runtest("\"\\n\"",
				new Token(STRING_LITERAL, 0, 0, "\\n"),
				new Token(EOF, 0, 4, ""));
	}

	@Test
	public void testSimpleStmts() {
		runtest("a = 1; b=2; while (a < b) { a = a + 1; }",
				new Token(ID, 0, 0, "a"),
				new Token(EQL, 0, 2, "="),
				new Token(INT_LITERAL, 0, 4, "1"),
				new Token(SEMICOLON, 0, 5, ";"),
				new Token(ID, 0, 7, "b"),
				new Token(EQL, 0, 8, "="),
				new Token(INT_LITERAL, 0, 9, "2"),
				new Token(SEMICOLON, 0, 10, ";"),

				new Token(WHILE, 0, 12, "while"),
				new Token(LPAREN, 0, 18, "("),
				new Token(ID, 0, 19, "a"),
				new Token(LT, 0, 21, "<"),
				new Token(ID, 0, 23, "b"),
				new Token(RPAREN, 0, 24, ")"),
				new Token(LCURLY, 0, 26, "{"),
				new Token(ID, 0, 28, "a"),
				new Token(EQL, 0, 30, "="),
				new Token(ID, 0, 32, "a"),
				new Token(PLUS, 0, 34, "+"),
				new Token(INT_LITERAL, 0, 36, "1"),
				new Token(SEMICOLON, 0, 37, ";"),
				new Token(RCURLY, 0, 39, "}"),

				new Token(EOF, 0, 40, ""));
	}

}

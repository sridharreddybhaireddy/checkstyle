package com.custom.checks;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.logging.FileHandler;
import java.util.logging.LoggingPermission;
import java.util.logging.SimpleFormatter;
import java.util.logging.Logger;

import com.puppycrawl.tools.checkstyle.api.*;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import com.puppycrawl.tools.checkstyle.*;
import com.puppycrawl.tools.checkstyle.checks.indentation.CatchHandler;
import com.puppycrawl.tools.checkstyle.checks.naming.MethodNameCheck;
import com.puppycrawl.tools.checkstyle.utils.CheckUtil;

public class InvalidReturnTypeFromFunction extends com.puppycrawl.tools.checkstyle.api.AbstractCheck {
	private final Logger logger = Logger.getLogger(InvalidReturnTypeFromFunction.class.getName());

	// private static Logger logger = Logger.getLogger(OnlyStaticVariables.class);

	public void visitToken(DetailAST ast) {
		Logger logger = Logger.getLogger("MyLog");

		FileHandler fh = null;

		SimpleDateFormat format = new SimpleDateFormat("M-d_HHmmss");
		try {
			fh = new FileHandler("C:/Windows/Temp/MyLogFile.log");
		} catch (Exception e) {
			e.printStackTrace();
		}

		fh.setFormatter(new SimpleFormatter());
		logger.addHandler(fh);

		try {

			/*
			 * for (DetailAST i = ast.getFirstChild(); i != null; i = i.getNextSibling()) {
			 * logger.info("Methods::" + i.getText()); }
			 */

			HashSet<String> hs = new HashSet<>();
			if (ast.findFirstToken(TokenTypes.SLIST) != null && ast.getParent() != null
					&& ast.getParent().getParent() != null
					&& ast.getParent().getParent().findFirstToken(TokenTypes.IMPLEMENTS_CLAUSE) != null
					&& ast.getParent().getParent().findFirstToken(TokenTypes.IMPLEMENTS_CLAUSE)
							.findFirstToken(TokenTypes.IDENT) != null) {
				/*
				 * implementsclassname =
				 * ast.getParent().getParent().findFirstToken(TokenTypes.IMPLEMENTS_CLAUSE)
				 * .findFirstToken(TokenTypes.IDENT).getText();
				 */
				for (DetailAST i = ast.getParent().getParent().findFirstToken(TokenTypes.IMPLEMENTS_CLAUSE)
						.findFirstToken(TokenTypes.IDENT); i != null; i = i.getNextSibling()) {
					if (i != null)
						hs.add(i.getText());
				}

			}
			String returnelementname = "";
			String funcname = "";
			if (ast.findFirstToken(TokenTypes.SLIST) != null
					&& ast.findFirstToken(TokenTypes.SLIST).findFirstToken(TokenTypes.LITERAL_RETURN) != null
					&& ast.findFirstToken(TokenTypes.SLIST).findFirstToken(TokenTypes.LITERAL_RETURN)
							.findFirstToken(TokenTypes.EXPR) != null
					&& ast.findFirstToken(TokenTypes.SLIST).findFirstToken(TokenTypes.LITERAL_RETURN)
							.findFirstToken(TokenTypes.EXPR).getFirstChild() != null) {

				returnelementname = ast.findFirstToken(TokenTypes.SLIST).findFirstToken(TokenTypes.LITERAL_RETURN)
						.findFirstToken(TokenTypes.EXPR).getFirstChild().getText();
			}
			if (ast.findFirstToken(TokenTypes.IDENT) != null)
				funcname = ast.findFirstToken(TokenTypes.IDENT).getText();

			if (funcname.equals("") || returnelementname.equals(""))
				return;
			if ((funcname.equals("invoke") && hs.contains("JavaService2")
					&& (returnelementname.equals("null") || returnelementname.equals("\"\"")))
					|| (funcname.equals("execute") && hs.contains("DataPostProcessor2")
							&& (returnelementname.equals("null") || returnelementname.equals("\"\"")))) {
				final DetailAST firstNode = (ast.findFirstToken(TokenTypes.SLIST)
						.findFirstToken(TokenTypes.LITERAL_RETURN).findFirstToken(TokenTypes.EXPR).getFirstChild());
				log(firstNode.getLineNo(), firstNode.getColumnNo(), "Error: Invalid return type for method " + funcname
						+ ", please change return type and try again.");

			}

			/*
			 * logger.info("SLIST VAL::" +
			 * ast.findFirstToken(TokenTypes.SLIST).findFirstToken(TokenTypes.
			 * LITERAL_RETURN) .findFirstToken(TokenTypes.EXPR).getFirstChild());
			 * logger.info("IDENT VAL"+ ast.findFirstToken(TokenTypes.IDENT) );
			 */

		} catch (Exception e) {
			logger.info("Error" + e);

		}

	}

	@Override
	public int[] getDefaultTokens() {
		// TODO Auto-generated method stub
		return new int[] { TokenTypes.METHOD_DEF };
	}

	@Override
	public int[] getAcceptableTokens() {
		// TODO Auto-generated method stub
		return new int[] { TokenTypes.METHOD_DEF };
	}

	@Override
	public int[] getRequiredTokens() {
		// TODO Auto-generated method stub
		return new int[] { TokenTypes.METHOD_DEF };
	}

}

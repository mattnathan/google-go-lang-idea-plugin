package ro.redeul.google.go.lang.lexer;

import com.intellij.lexer.DelegateLexer;
import com.intellij.lexer.Lexer;
import com.intellij.lexer.LexerPosition;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

/**
 * Created by IntelliJ IDEA.
 * User: mtoader
 * Date: Jul 25, 2010
 * Time: 12:56:23 AM
 * To change this template use File | Settings | File Templates.
 */
public class GoFixElidedSemiLexer extends DelegateLexer implements GoTokenTypes {

    IElementType lastElementType;

    TokenSet elidingTokens = TokenSet.create(
            mIDENT,
            litINT, litOCT, litHEX, litFLOAT,
            litDECIMAL_I, litFLOAT_I,
            litCHAR, litSTRING,
            kBREAK, kCONTINUE, kFALLTHROUGH, kRETURN,
            oPLUS_PLUS, oMINUS_MINUS,
            pRCURLY, pRBRACK, pRPAREN
    );

    public GoFixElidedSemiLexer(Lexer delegate) {
        super(delegate);
    }

    @Override
    public void advance() {        
        if ( ! isElidedPlace() ) {
            lastElementType = super.getTokenType() == wsWS ? lastElementType : super.getTokenType(); 
            super.advance();
        } else {
            lastElementType = getTokenType();
        }
    }

    @Override
    public IElementType getTokenType() {
        if (isElidedPlace()) {
            return oSEMI;
        }

        return super.getTokenType();
    }

    @Override
    public int getTokenEnd() {
        if ( isElidedPlace() ) {
            return super.getTokenStart();
        }

        return super.getTokenEnd();
    }

    @Override
    public void start(CharSequence buffer, int startOffset, int endOffset, int initialState) {
        super.start(buffer, startOffset, endOffset, initialState);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void restore(LexerPosition position) {
        super.restore(position);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public LexerPosition getCurrentPosition() {
        return super.getCurrentPosition();    //To change body of overridden methods use File | Settings | File Templates.
    }

    private boolean isElidedPlace() {
        IElementType actualToken = super.getTokenType();
        return actualToken != null && lastElementType != null &&
                actualToken.equals(GoTokenTypes.wsNLS) && elidingTokens.contains(lastElementType);
    }
}

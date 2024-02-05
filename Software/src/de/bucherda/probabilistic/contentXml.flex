package de.bucherda.probabilistic;
import java_cup.runtime.*;

%%

%cup
%line
%column
%unicode
%class ContentLexer

%{

/**
 * Return a new Symbol with the given token id, and with the current line and
 * column numbers.
 */
Symbol newSym(int tokenId) {
    return new Symbol(tokenId, yyline, yycolumn);
}

/**
 * Return a new Symbol with the given token id, the current line and column
 * numbers, and the given token value.  The value is used for tokens such as
 * identifiers and numbers.
 */
Symbol newSym(int tokenId, Object value) {
    return new Symbol(tokenId, yyline, yycolumn, value);
}

%}

/* basic Defs */
STRING          = [^\"]+

QUOT            = \"

/* KEYWORD */
/* open */
element_O       = "<element>"
attribute_O     = "<attribute>"
group_O         = "<group>"
interleave_O    = "<interleave>"
choice_O        = "<choice>"
optional_O      = "<optional>"
zeroOrMore_O    = "<zeroOrMore>"
oneOrMore_O     = "<oneOrMore>"
list_O          = "<list>"
mixed_O         = "<mixed>"
except_O        = "<except>"
anyName_O       = "<anyName>"
nsName_O        = "<nsName>"
choice_O        = "<choice>"
name_O          = "<name>"
start_O         = "<start>"

/* close */
element_C       = "</element>"
attribute_C     = "</attribute>"
group_C         = "</group>"
interleave_C    = "</interleave>"
choice_C        = "</choice>"
optional_C      = "</optional>"
zeroOrMore_C    = "</zeroOrMore>"
oneOrMore_C     = "</oneOrMore>"
list_C          = "</list>"
mixed_C         = "</mixed>"
grammar_C       = "</grammar>"
except_C        = "</except>"
data_C          = "</data>"
define_C        = "</define>"
anyName_C       = "</anyName>"
nsName_C        = "</nsName>"
choice_C        = "</choice>"
name_C          = "</name>"
start_C         = "</start>"

/* other */
head            = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
empty           = "<empty/>"
text            = "<text/>"
notAllowed      = "<notAllowed/>"
element         = "<element name="{QUOT}{STRING}{QUOT}">"
ref             = "<ref name="{QUOT}{STRING}{QUOT}"/>"
parentRef       = "<parentRef name="{QUOT}{STRING}{QUOT}
externalRef     = "<externalRef href="{QUOT}{STRING}{QUOT}
anyName         = "<anyName/>"

data            = "<data type="{QUOT}{STRING}{QUOT}"/>"
                | "<data type="{QUOT}{STRING}{QUOT}">"

attribute       = "<attribute name="\"[^\"]*\"([ \t\r\n\f])*([^\r\n]+"="\"[^\"]*\")?(">"|"/>")

 /*               "<attribute name="{QUOT}{STRING}{QUOT}">"
                | "<attribute name="{QUOT}{STRING}{QUOT}"/>"
                | "<attribute name="{QUOT}{STRING}{QUOT}{whiteSpace}[^=]+"="{QUOT}{STRING}{QUOT}">"*/
name            = "<name>"[^<]+"</name>"
value           = "<value>"[^<]+"</value>"
param           = "<param name="{QUOT}{STRING}{QUOT}">"[^<]+"</param>"
define          = "<define name="{QUOT}{STRING}{QUOT}{whiteSpace}"combine="{QUOT}{STRING}{QUOT}">"
                | "<define name="{QUOT}{STRING}{QUOT}">"

grammar         = "<grammar"

closeTag        = ">"
endTag          = "/>"

attr            = [^>]+">"

newLines		= \n|\r|\r\n
inputChar		= [^\r\n]
whiteSpace		= ({newLines}|[ \t\f])*

htmlComment_O		= "<!--"
htmlComment_C		= "-->"


/* deep r  */

%state STRING, ATTR, COMMENT

%%
<YYINITIAL> {
{htmlComment_O}         { yybegin(COMMENT);/* ignore */ }
{whiteSpace}			{ /* ignore */ }

{QUOT}                  { yybegin(STRING); return newSym(sym.QM, "ANF_AUF");}
{element_O}             { return newSym(sym.ELEMENT_O, yytext());}
{attribute_O}           { return newSym(sym.ATTRIBUTE_O, yytext());}
{group_O}               { return newSym(sym.GROUP_O, yytext());}
{interleave_O}          { return newSym(sym.INTERLEAVE_O, yytext());}
{choice_O}              { return newSym(sym.CHOICE_O, yytext());}
{optional_O}            { return newSym(sym.OPTIONAL_O, yytext());}
{zeroOrMore_O}          { return newSym(sym.ZEROORMORE_O, yytext());}
{oneOrMore_O}           { return newSym(sym.ONEORMORE_O, yytext());}
{list_O}                { return newSym(sym.LIST_O, yytext());}
{mixed_O}               { return newSym(sym.MIXED_O, yytext());}
{except_O}              { return newSym(sym.EXCEPT_O, yytext());}
{anyName_O}             { return newSym(sym.ANYNAME_O, yytext());}
{nsName_O}              { return newSym(sym.NSNAME_O, yytext());}
{choice_O}              { return newSym(sym.CHOICE_O, yytext());}
{name_O}                { return newSym(sym.NAME_O, yytext());}
{start_O}                { return newSym(sym.START_O, yytext());}

{element_C}             { return newSym(sym.ELEMENT_C, yytext());}
{attribute_C}           { return newSym(sym.ATTRIBUTE_C, yytext());}
{group_C}               { return newSym(sym.GROUP_C, yytext());}
{interleave_C}          { return newSym(sym.INTERLEAVE_C, yytext());}
{choice_C}              { return newSym(sym.CHOICE_C, yytext());}
{optional_C}            { return newSym(sym.OPTIONAL_C, yytext());}
{zeroOrMore_C}          { return newSym(sym.ZEROORMORE_C, yytext());}
{oneOrMore_C}           { return newSym(sym.ONEORMORE_C, yytext());}
{list_C}                { return newSym(sym.LIST_C, yytext());}
{mixed_C}               { return newSym(sym.MIXED_C, yytext());}
{except_C}              { return newSym(sym.EXCEPT_C, yytext());}
{data_C}                { return newSym(sym.DATA_C, yytext());}
{define_C}              { return newSym(sym.DEFINE_C, yytext());}
{anyName_C}             { return newSym(sym.ANYNAME_C, yytext());}
{nsName_C}              { return newSym(sym.NSNAME_C, yytext());}
{choice_C}              { return newSym(sym.CHOICE_C, yytext());}
{name_C}                { return newSym(sym.NAME_C, yytext());}
{grammar_C}             { return newSym(sym.GRAMMAR_C, yytext());}
{start_C}               { return newSym(sym.START_C, yytext());}

{head}                  { return newSym(sym.HEAD, yytext());}
{empty}                 { return newSym(sym.EMPTY, yytext());}
{text}                  { return newSym(sym.TEXT, yytext());}
{notAllowed}            { return newSym(sym.NOTALLOWED, yytext());}
{element}               { return newSym(sym.ELEMENT, yytext());}
{attribute}             { return newSym(sym.ATTRIBUTE, yytext());}
{ref}                   { return newSym(sym.REF, yytext());}
{parentRef}             { return newSym(sym.PARENTREF, yytext());}
{data}                  { return newSym(sym.DATA, yytext());}
{externalRef}           { return newSym(sym.EXTERNALREF, yytext());}
{define}                { return newSym(sym.DEFINE, yytext());}
{anyName}               { return newSym(sym.ANYNAME, yytext());}
{closeTag}              { return newSym(sym.CLOSETAG, yytext());}
{endTag}                { return newSym(sym.ENDTAG, yytext());}
{value}                 { return newSym(sym.VALUE, yytext());}
{name}                  { return newSym(sym.NAME, yytext());}
{param}                 { return newSym(sym.PARAM, yytext());}
{grammar}               { yybegin(ATTR); return newSym(sym.GRAMMAR, yytext());}
}

<STRING> {
{STRING}                { return newSym(sym.STRING, yytext());}
{QUOT}                  { yybegin(YYINITIAL); return newSym(sym.QM, "ANF_ZU");}
}

<ATTR> {
{whiteSpace}			{ /* ignore */ }
{attr}                  { yybegin(YYINITIAL); return newSym(sym.ATTR, yytext());}
}

<COMMENT> {
[^]                     { /* ignore */}
{htmlComment_C}         { yybegin(YYINITIAL);}
}

[^]						{ return newSym(sym.ZERO, "ERROR: "+yytext());}

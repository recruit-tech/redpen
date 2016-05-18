package cc.redpen.parser;

import cc.redpen.RedPenException;
import cc.redpen.config.Configuration;
import cc.redpen.model.Document;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


public class PreprocessorTest {
    private static final Logger LOG = LoggerFactory.getLogger(PreprocessorTest.class);

    @Test
    public void testSuppressErrors() throws UnsupportedEncodingException, RedPenException {
        String sampleText = "Instances Overview\n==================\n" + "Author's Name <person@email.address>\nv1.2, 2015-08\n" +
            "\nThis is the optional preamble (an untitled section body). Useful for " +
            "writing simple sectionless documents consisting only of a preamble.\n\n" +
            "[suppress]\n" +

            "NOTE: The abstract, preface, appendix, bibliography, glossary and index section titles are significant ('specialsections').\n" +

            "\n\n:numbered!:\n[abstract]\n" +
            "[suppress='SuppressRuleParameter']\n" +
            "Instances\n" +
            "---------\n" +
            "In this article, we'll call a computer server that works as a member of a cluster an _instan3ce_. " +
            "for example, as shown in this http://redpen.ignored.url/[mishpelled link], each instance in distributed search engines stores the the fractions of data.\n" +
            "\nSuch distriubuted systems need a component to merge the preliminary results from member instnaces.\n\n\n" +
            ".Instance image\n" +
            "image::images/tiger.png[Instance image]\n\n" +
            "A sample table:\n\n" +
            ".A sample table\n" +
            "[width=\"60%\",options=\"header\"]\n" +
            "|==============================================\n" +
            "| Option     | Description\n" +
            "| GROUP      | The instance group.\n" +
            "|==============================================\n\n" +
            ".example list\n" +
            "===============================================\n" +
            "Lorum ipum...\n" +
            "===============================================\n\n\n" +
            "[bibliography]\n" +
            "- [[[taoup]]] Eric Steven Raymond. 'The Art of Unix\n" +
            "  Programming'. Addison-Wesley. ISBN 0-13-142901-9.\n" +
            "- [[[walsh-muellner]]] Norman Walsh & Leonard Muellner.\n" +
            "  'DocBook - The Definitive Guide'. O'Reilly & Associates. 1999.\n" +
            "  ISBN 1-56592-580-7.\n\n\n" +
            "[glossary]\n" +
            "Example Glossary\n" +
            "----------------\n" +
            "Glossaries are optional. Glossaries enries are an example of a style\n" +
            "of AsciiDoc labeled lists.\n" +
            "[suppress='SuccessiveWord InvalidExpression Spelling']\n" +
            "The following is an example of a glosssary.\n\n" +
            "[glossary]\n" +
            "A glossary term::\n" +
            "  The corresponding (indented) defnition.\n\n" +
            "A second glossary term::\n" +
            "  The corresponding (indented) definition.\n\n\n" +
            "ifdef::backend-docbook[]\n" +
            "[suppress=without using any quotes]\n" +
            "[index]\n" +
            "Example Index\n" +
            "-------------\n" +
            "////////////////////////////////////////////////////////////////\n" +
            "The index is normally left completely empty, it's contents being\n" +
            "generated automatically by the DocBook toolchain.\n" +
            "////////////////////////////////////////////////////////////////\n" +
            "endif::backend-docbook[]";


        Document doc = createFileContent(sampleText, DocumentParser.ASCIIDOC);
        assertEquals(4, doc.getPreprocessorRules().size());

        doc = createFileContent(sampleText, DocumentParser.PLAIN);
        assertEquals(0, doc.getPreprocessorRules().size());

        doc = createFileContent(sampleText, DocumentParser.LATEX);
        assertEquals(0, doc.getPreprocessorRules().size());

        doc = createFileContent(sampleText, DocumentParser.MARKDOWN);
        assertEquals(0, doc.getPreprocessorRules().size());

        doc = createFileContent(sampleText, DocumentParser.REVIEW);
        assertEquals(0, doc.getPreprocessorRules().size());
    }


    private Document createFileContent(String inputDocumentString, DocumentParser parser) {
        Document doc = null;
        try {
            Configuration configuration = Configuration.builder().build();
            doc = parser.parse(
                inputDocumentString,
                new SentenceExtractor(configuration.getSymbolTable()),
                configuration.getTokenizer());
        } catch (RedPenException e) {
            e.printStackTrace();
            fail();
        }
        return doc;
    }
}
/**
 * (c) 2003-2014 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.jsonsanitizer;

import static org.junit.Assert.*;

import org.mule.api.MuleEvent;
import org.mule.tck.junit4.FunctionalTestCase;

import org.junit.Test;

public class JsonSanitizerConnectorTest extends FunctionalTestCase
{
	private void assertSanitized(String golden, String input) throws Exception {
	    MuleEvent me = runFlow("testFlow",input);
	    assertEquals(golden, me.getMessage().getPayload());
	  }

	  private void assertSanitized(String sanitary) throws Exception {
	    assertSanitized(sanitary, sanitary);
	  }

    @Override
    protected String getConfigResources()
    {
        return "jsonsanitizer-config.xml";
    }

    @Test
    public void testFlow() throws Exception
    {
    	//runFlowWithPayloadAndExpect("testFlow", "it", "it");
    	// On the left is the sanitized output, and on the right the input.
        // If there is a single string, then the input is fine as-is.
      assertSanitized("null", null);
        assertSanitized("null", "");
        assertSanitized("null");
        assertSanitized("false");
        assertSanitized("true");
        assertSanitized(" false ");
        assertSanitized("  false");
        assertSanitized("false\n");
        assertSanitized("false", "false,true");
        
        assertSanitized("\"foo\"");
        assertSanitized("\"foo\"", "'foo'");
        assertSanitized(
            "\"<script>foo()<\\/script>\"", "\"<script>foo()</script>\"");
        assertSanitized(
            "\"<script>foo()<\\/script>\"", "\"<script>foo()</script>\"");
        assertSanitized("\"<\\/SCRIPT\\n>\"", "\"</SCRIPT\n>\"");
        assertSanitized("\"<\\/ScRIpT\"", "\"</ScRIpT\"");
        // \u0130 is a Turkish dotted upper-case 'I' so the lower case version of
        // the tag name is "script".
        assertSanitized("\"<\\/ScR\u0130pT\"", "\"</ScR\u0130pT\"");
        assertSanitized("\"<b>Hello</b>\"");
        assertSanitized("\"<s>Hello</s>\"");
        assertSanitized("\"<[[\\u005d]>\"", "'<[[]]>'");
        assertSanitized("\"\\u005d]>\"", "']]>'");
        assertSanitized("[[0]]", "[[0]]>");
        assertSanitized("[1,-1,0.0,-0.5,1e2]", "[1,-1,0.0,-0.5,1e2,");
        assertSanitized("[1,2,3]", "[1,2,3,]");
        assertSanitized("[1,null,3]", "[1,,3,]");
        assertSanitized("[1 ,2 ,3]", "[1 2 3]");
        assertSanitized("{ \"foo\": \"bar\" }");
        assertSanitized("{ \"foo\": \"bar\" }", "{ \"foo\": \"bar\", }");
        assertSanitized("{\"foo\":\"bar\"}", "{\"foo\",\"bar\"}");
        assertSanitized("{ \"foo\": \"bar\" }", "{ foo: \"bar\" }");
        assertSanitized("{ \"foo\": \"bar\"}", "{ foo: 'bar");
        
    }
}

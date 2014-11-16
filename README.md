# JSON Sanitizer Transformer
The JSON Sanitizer transformer is used as a first pass on untrusted JSON Strings. 

It utilizes OWASP's JSON Sanitizer project.


# Mule supported versions
Mule 3.5.x


# Installation 
For beta connectors you can download the source code and build it with DevKit to find it available on your local repository. Then you can add it to Studio via Help ->Install New Software -> Add -> Enter JSON Sanitizer for the Name and click on Local -> Choose the Path to your freshly built Update Site that was created previously during the transformers build.

For released connectors you can download them from the update site in Studio. 
Open MuleStudio, go to Help → Install New Software and select MuleStudio Cloud Connectors Update Site where you’ll find all avaliable connectors.

#Usage
Pass a JSON String to this transformer and it will return a sanitized JSON String back.

<jsonsanitizer:sanitize />

##Input
The sanitizer takes JSON like content, and interprets it as JS eval would. Specifically, it deals with these non-standard constructs.

* '...'	Single quoted strings are converted to JSON strings.
* \xAB	Hex escapes are converted to JSON unicode escapes.
* \012	Octal escapes are converted to JSON unicode escapes.
* 0xAB	Hex integer literals are converted to JSON decimal numbers.
* 012	Octal integer literals are converted to JSON decimal numbers.
* +.5	Decimal numbers are coerced to JSON's stricter format.
* [0,,2]	Elisions in arrays are filled with null.
* [1,2,3,]	Trailing commas are removed.
* {foo:"bar"}	Unquoted property names are quoted.
* //comments	JS style line and block comments are removed.
* (...)	Grouping parentheses are removed.

The sanitizer fixes missing punctuation, end quotes, and mismatched or missing close brackets. If an input contains only white-space then the valid JSON string null is substituted.

##Output
The output is well-formed JSON as defined by RFC 4627. The output satisfies three additional properties:

The output will not contain the substring (case-insensitively) "</script" so can be embedded inside an HTML script element without further encoding.
The output will not contain the substring "]]>" so can be embedded inside an XML CDATA section without further encoding.
The output is a valid Javascript expression, so can be parsed by Javascript's eval builtin (after being wrapped in parentheses) or by JSON.parse. Specifically, the output will not contain any string literals with embedded JS newlines (U+2028 Paragraph separator or U+2029 Line separator).
The output contains only valid Unicode scalar values (no isolated UTF-16 surrogates) that are allowed in XML unescaped.

##Security
Since the output is well-formed JSON, passing it to JavaScript's 'eval will have no side-effects and no free variables, so is neither a code-injection vector, nor a vector for exfiltration of secrets.

The transform only ensures that the JSON string → Javascript object phase has no side effects and resolves no free variables, and cannot control how other client side code later interprets the resulting Javascript object. So if client-side code takes a part of the parsed data that is controlled by an attacker and passes it back through a powerful interpreter like eval or innerHTML then that client-side code might suffer unintended side-effects.

# Reporting Issues

We use GitHub:Issues for tracking issues with this connector. You can report new issues at this link https://github.com/jakecmorgan-mule/json-sanitizer-transformer/issues.
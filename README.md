Thymeleaf Jawr Extension
========================

```xml
<dependency>
	<groupId>com.github.dtrunk90</groupId>
	<artifactId>thymeleaf-jawr-extension</artifactId>
	<version>1.2.0</version>
</dependency>
```

Attributes
----------

All attributes need to be prefixed by `jawr:`.<br />
To avoid IDE warnings you can add the namespace as follows:
```html
<html xmlns:jawr="http://jawr.java.net" xmlns:th="http://www.thymeleaf.org"></html>
```

Javascript attributes:

| Attribute name | Type    | Purpose                                                            | Default value   |
| :------------- | :------ | :----------------------------------------------------------------- | :-------------- |
| src            | String  | The bundle path.                                                   |                 |
| useRandomParam | Boolean | The flag indicating if we must use random parameter in debug mode. | true            |
| async          | Boolean | The async flag.                                                    | false           |
| defer          | Boolean | The defer flag.                                                    | false           |

CSS attributes:

| Attribute name   | Type    | Purpose                                                                             | Default value |
| :--------------- | :------ | :---------------------------------------------------------------------------------- | :------------ |
| href             | String  | The bundle path.                                                                    |               |
| media            | String  | The media attribute of the stylesheet.                                              | screen        |
| title            | String  | The title to use for the style.                                                     |               |
| useRandomParam   | Boolean | The flag indicating if we must use random parameter in debug mode.                  | true          |
| alternate        | Boolean | This flag is used to render link as an alternate style.                             | false         |
| displayAlternate | Boolean | This flag is used to render the skin variants of the CSS bundle as alternate style. | false         |

Expression object `#jawr`
--------------------------

```java
${#jawr.imagePath(String src)}						// base64 defaults to false
${#jawr.imagePath(String src, boolean base64)}
```

It's important to note that Jawr will generate the base64 encoded image for all browsers except IE6 and IE7, which doesn't handle base64 encoded images.

Usage examples
--------------

Javascript bundle:
```html
<script type="text/javascript" src="jquery.min.js" jawr:src="/lib.js"></script>
```

CSS bundle:
```html
<link rel="stylesheet/less" type="text/css" href="main.less" jawr:href="/all.css" />
```

Image:
```html
<img src="../resources/img/dummy.png" alt="" th:src="${#jawr.imagePath('/resources/img/dummy.png')}" />
```

Image input:
```html
<input type="image" src="../resources/img/dummy.png" th:src="${#jawr.imagePath('/resources/img/dummy.png')}" />
```

You can use expressions as well:
```html
<script type="text/javascript" src="index.js" jawr:src="|/${pageName}.js|"></script>
```
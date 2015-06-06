Thymeleaf Jawr Dialect
======================

Attributes
----------

All attributes need to be prefixed by `jawr:`.<br />
To avoid IDE warnings you can add the namespace as follows:
```html
<html xmlns:jawr="http://jawr.java.net" xmlns:th="http://www.thymeleaf.org"></html>
```

Javascript attributes:

| Attribute name | Type    | Purpose                                                            | Default value |
| :------------- | :------ | :----------------------------------------------------------------- | :------------ |
| src            | String  | The bundle path.                                                   |               |
| useRandomParam | Boolean | The flag indicating if we must use random parameter in debug mode. | true          |
| async          | Boolean | The async flag.                                                    | false         |
| defer          | Boolean | The defer flag.                                                    | false         |

CSS attributes:

| Attribute name   | Type    | Purpose                                                                             | Default value |
| :--------------- | :------ | :---------------------------------------------------------------------------------- | :------------ |
| href             | String  | The bundle path.                                                                    |               |
| media            | String  | The media attribute of the stylesheet.                                              |               |
| title            | String  | The title to use for the style.                                                     |               |
| useRandomParam   | Boolean | The flag indicating if we must use random parameter in debug mode.                  | true          |
| alternate        | Boolean | This flag is used to render link as an alternate style.                             | false         |
| displayAlternate | Boolean | This flag is used to render the skin variants of the CSS bundle as alternate style. | false         |

Usage examples
--------------

Javascript bundle:
```html
<script type="text/javascript" src="jquery-1.11.1.min.js" jawr:src="/lib.js"></script>
```

CSS bundle:
```html
<link rel="stylesheet/less" type="text/css" href="main.less" jawr:href="/all.css" />
```

You can use expressions as well:
```html
<script type="text/javascript" src="index.js" jawr:src="|/${pageName}.js|"></script>
```
Thymeleaf Jawr Dialect
======================

Usage examples
--------------

```html
<link rel="stylesheet/less" type="text/css" href="main.less" jawr:style="'/all.css'" />
<script type="text/javascript" src="jquery-1.11.1.min.js" jawr:script="'/lib.js'"></script>
<script type="text/javascript" src="less-1.7.5.min.js" th:remove="all"></script>
<script type="text/javascript" src="main.js" jawr:script="'/all.js'"></script>
<script type="text/javascript" src="index.js" jawr:script="|/${pageName}.js|"></script>
```
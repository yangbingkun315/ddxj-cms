// global object alyCms
var alyCms = {};
alyCms.base = {};
alyCms.util = {};
var noLoginCallback = [];
// 验证邮箱正则
var PATTERN_EMAIL = /.+@.+\.[a-zA-Z]{2,4}$/;
// 验证密码正则
var PATTERN_PASSWORD = /([a-z]|[A-Z]|[0-9]|[,.;<>:!@#$%^&*()_+-=]){6,16}/;
// form field attr
var FORM_FIELD_MAXLENGTH = "maxLength";
var FORM_FIELD_MINLENGTH = "minLength";
var FORM_FIELD_MINVALUE = "minValue";
var FORM_FIELD_MAXVALUE = "maxValue";
var FORM_FIELD_REQUIRED = "isRequired";
var FORM_FIELD_EQUALTO = "equalTo";
var FORM_FIELD_FIELDTYPE = "fieldType";
var FORM_FIELD_BLANK_TEXT = "blankText";
var FORM_FIELD_LENGTH_TEXT = "lengthText";
var FORM_FIELD_RIGHT_TEXT = "rightText";
var FORM_FIELD_FORM_ERROR_TEXT = "formErrorText";
var FORM_FIELD_TYPE_ERROR_TEXT = "typeErrorText";
var FORM_FIELD_SELECT_NULL_VALUE = "nullValue";
var FORM_FIELD_RIGHT_MESSAGE = "rightMessage";
var FORM_FIELD_REPLACE_HOLDER = "placeholder";
var FORM_FIELD_SHOW_RIGHT_MSG = "showRightMsg";
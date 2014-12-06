<?php
$msgid = $_POST['username'];
$fname = $_POST['fname'];
$lname = $_POST['lname'];
$msgid=mysql_real_escape_string($msgid);
$msgid=trim($msgid," \t\n\r\0\x0B");
$fname=mysql_real_escape_string($fname);
$lname=mysql_real_escape_string($lname);
$link = mysql_connect('localhost:3306', 'root', '');
if (!$link) {
    die('Could not connect: ' . mysql_error());
}
mysql_query("SET SESSION time_zone = '+22:00'"); 
mysql_select_db("imagesharingdatabase", $link);

$result=mysql_query("SELECT * from user_details WHERE username='$msgid'");
echo "before: $result msgid: $msgid ";
$result=mysql_num_rows($result);
echo " after: $result";
if($result==0)
{
	$result=mysql_query("INSERT INTO user_details VALUES('$msgid',100,0,'".date("Y-m-d H:i:s", time())."','$fname','$lname')");
}
else
{
	$result=mysql_query("UPDATE user_details SET fname='$fname' WHERE username='$msgid'");
	$result=mysql_query("UPDATE user_details SET lname='$lname' WHERE username='$msgid'");
}
mysql_close($link);
?>

<?php
$msgid = $_POST['username'];
$msgid=mysql_real_escape_string($msgid);
$msgid=trim($msgid," \t\n\r\0\x0B");
$link = mysql_connect('localhost:3306', 'root', '');
if (!$link) {
    die('Could not connect: ' . mysql_error());
}
mysql_query("SET SESSION time_zone = '+22:00'"); 
mysql_select_db("imagesharingdatabase", $link);
$result=mysql_query("SELECT * from user_details WHERE username='$msgid'");
$numrow=mysql_num_rows($result);
echo "before: $numrow msgid: $msgid result: $result \n ";
//$result=mysql_num_rows($result);
//echo "before: $result msgid: $msgid \n ";
if($numrow!=0)
{
	
	$row=mysql_fetch_array($result);
	
	print json_encode($row);
}
mysql_close($link);
?>

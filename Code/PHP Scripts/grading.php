<?php
$username = "s2381410";
$password = "s2381410";
$database = "d2381410";
$link = mysqli_connect("127.0.0.1", $username, $password, $database);
$group = $_REQUEST["GROUP_NAME"];
$task = $_REQUEST["ASSESSMENT"];
$score = $_REQUEST["SCORE"];
$total = $_REQUEST["TOTAL"];

$output=array();
/* Select queries return a resultset */
if ($r = mysqli_query($link, "INSERT INTO GRADES VALUES('$group', '$task', '$score', '$total')")) {
while ($row=$r->fetch_assoc()){
$output[]=$row;
}
}
mysqli_close($link);
echo json_encode($output);
?>

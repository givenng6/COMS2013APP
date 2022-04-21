<?php
$username = "s2381410";
$password = "s2381410";
$database = "d2381410";
$link = mysqli_connect("127.0.0.1", $username, $password, $database);
$assessment = $_REQUEST["ASSESSMENT_NAME"];
$output=array();
/* Select queries return a resultset */
if ($r = mysqli_query($link, "SELECT * FROM GROUPS WHERE
ASSESSMENT_NAME='$assessment'")) {
while ($row=$r->fetch_assoc()){
$output[]=$row;
}
}
mysqli_close($link);
echo json_encode($output);
?>

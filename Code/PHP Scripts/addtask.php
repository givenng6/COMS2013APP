<?php
$username = "s2381410";
$password = "s2381410";
$database = "d2381410";
$link = mysqli_connect("127.0.0.1", $username, $password, $database);
$name = $_REQUEST["TASK_NAME"];
$creator = $_REQUEST["CREATOR"];
$total = $_REQUEST["GRAND_TOTAL"];
$output=array();

if ($r = mysqli_query($link, "INSERT INTO ASSESSMENTS VALUES('$name', '$creator', '$total')")) {
while ($row=$r->fetch_assoc()){
$output[]=$row;
}
}
mysqli_close($link);
echo json_encode($output);
?>

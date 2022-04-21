<?php
$username = "s2381410";
$password = "s2381410";
$database = "d2381410";
$link = mysqli_connect("127.0.0.1", $username, $password, $database);

$table = $_REQUEST["TABLE"];
$col = $_REQUEST["COL"];
$input = $_REQUEST["INPUT"];

$output=array();

if ($r = mysqli_query($link, "DELETE FROM $table WHERE $col = '$input'")) {
while ($row=$r->fetch_assoc()){
$output[]=$row;
}
}
mysqli_close($link);
echo json_encode($output);
?>

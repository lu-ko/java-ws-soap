// <![CDATA[
/**
 * Disables web page interaction. It is used after button click / form submit.
 * 
 * Usage: onsubmit="disablePage();"
 */
function disablePage() {
	document.getElementById("overlay-block").style.display='block';
	return true;
}
// ]]>

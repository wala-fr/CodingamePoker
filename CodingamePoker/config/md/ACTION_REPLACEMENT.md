<h1>ACTION REPLACEMENT</h1>
<!--<style>
table, td, th {
	border: 1px solid #000;
	border-collapse: collapse;
}
</style>-->
<table style="border-collapse: collapse">
	<tr height="80">
		<th  style="border: 2px solid" align="center">player action</th>
		<th  style="border: 2px solid" align="center">replacement action</th>
		<th  style="border: 2px solid" align="center">explanation</th>
		<th  style="border: 2px solid" align="center">example</th>
	</tr>
	<tr>
		<td  style="border: 2px solid" align="left" colspan="4" height="80"><u><b style="color:red">WARNING</b></u> : <b>FOLD</b> is never replaced. Even if a <b>CHECK</b> is possible.
		</td>
	</tr>
	<tr>
		<td  style="border: 2px solid" align="center"><b>CALL</b></td>
		<td  style="border: 2px solid" align="center"><b>ALL-IN</b></td>
		<td  style="border: 2px solid" align="left">Player hasn't enough chips to <b>CALL</b>.
		</td>
		<td  style="border: 2px solid" align="left">FLOP
			<ol>
				<li>player 0 : <b>BET 200</b></li>
				<li>player 1 : <b>CALL</b> (with a stack of $100)
				</li>
			</ol>
		</td>
	</tr>
	<tr>
		<td  style="border: 2px solid" align="center"><b>CALL</b></td>
		<td  style="border: 2px solid" align="center"><b>CHECK</b></td>
		<td  style="border: 2px solid" align="left">Player can only <b>CHECK</b> or <b>RAISE</b>.
		</td>
		<td  style="border: 2px solid" align="left">PRE-FLOP with a big blind of $10
			<ol>
				<li>player 1 : <b>CALL</b> $5
				</li>
				<li>player 2 : <b>CALL</b> $5
				</li>
				<li>player 0 (big blind for $10) : can only <b>CHECK</b> or <b>RAISE</b></li>
			</ol>
		</td>
	</tr>
	<tr>
		<td  style="border: 2px solid" align="center"><b>BET</b> / <b>ALL-IN</b></td>
		<td  style="border: 2px solid" align="center"><b>CALL</b></td>
		<td  style="border: 2px solid" align="left">Player can only <b>CALL</b> or <b>FOLD</b>,
			since he was<br /> <b>the last one to make a 'proper' raise</b>.
		</td>
		<td  style="border: 2px solid" align="left">FLOP
			<ol>
				<li>player 0 (with a stack of $1000) : <b>BET 200</b></li>
				<li>player 1 : <b>ALL-IN</b> $300 (not a proper raise since
					the raise is $100 less the last raise $200)
				</li>
				<li>player 2 : <b>CALL</b> $300
				</li>
				<li>player 0 : can only <b>FOLD</b> or <b>CALL</b> since he's
					the last one to raise
				</li>
			</ol>
		</td>
	</tr>
	<tr>
		<td  style="border: 2px solid" align="center"><b>BET</b></td>
		<td  style="border: 2px solid" align="center"><b>ALL-IN</b></td>
		<td  style="border: 2px solid" align="left">Player hasn't enough chips to bet the given
			amount.</td>
		<td  style="border: 2px solid" align="left">FLOP
			<ol>
				<li>player 0 : <b>BET 200</b> (with a stack of $100)
				</li>
			</ol>
		</td>
	</tr>
	<tr>
		<td  style="border: 2px solid" align="left" colspan="4" height="80">The basic <b>BET</b>
			rule is : if you put chip in the pot you <b style="color:red">can't take them back</b>.You
			have to complete the bet so it's legal.
		</td>
	</tr>
	<tr>
		<td  style="border: 2px solid" align="center" rowspan="2"><b>BET</b></td>
		<td  style="border: 2px solid" align="center"><b>CALL</b></td>
		<td  style="border: 2px solid" align="left" rowspan="2">The <b>BET</b> amount is less than the <b>CALL</b> amount.
		</td>
		<td  style="border: 2px solid" align="left">FLOP
			<ol>
				<li>player 0 : <b>BET 100</b>
				</li>
				<li>player 1 : <b>BET 50</b> replaced by <b>CALL</b> (same as <b>BET 100</b>).
				</li>
			</ol>
		</td>
	</tr>
	<tr>
		<td  style="border: 2px solid" align="center"><b>ALL-IN</b></td>
		<td  style="border: 2px solid" align="left">FLOP
			<ol>
				<li>player 0 : <b>BET 100</b>
				</li>
				<li>player 1 (with a stack of $75) : <b>BET 50</b> replaced by <b>ALL-IN</b>.
				</li>
			</ol>
		</td>
	</tr>
	<tr>
		<td  style="border: 2px solid" align="center" rowspan="2"><b>BET</b></td>
		<td  style="border: 2px solid" align="center"><b>BET</b></td>
		<td  style="border: 2px solid" align="left" rowspan="2">The bet amount must <b>at least
				be one big blind</b>.
		</td>
		<td  style="border: 2px solid" align="left">FLOP with a big blind of $10
			<ol>
				<li>player 0 : <b>BET 5</b> (should be at least $10)<br />=>
					replaced by <b>BET 10</b>
				</li>
			</ol>
		</td>
	</tr>
	<tr>
		<td  style="border: 2px solid" align="center"><b>ALL-IN</b></td>
		<td  style="border: 2px solid" align="left">FLOP with a big blind of $10
			<ol>
				<li>player 0 : <b>BET 5</b> with a stack of $7 (should be at
					least $10)<br />=> replaced by <b>ALL-IN</b>
				</li>
			</ol>
		</td>
	</tr>
	<tr>
		<td  style="border: 2px solid" align="center" rowspan="2"><b>BET</b></td>
		<td  style="border: 2px solid" align="center"><b>BET</b></td>
		<td  style="border: 2px solid" align="left" rowspan="2">The bet amount must <b>at least
				be the last raise</b>.
		</td>
		<td  style="border: 2px solid" align="left">FLOP
			<ol>
				<li>player 0 : <b>BET 200</b></li>
				<li>player 1 : <b>BET 300</b><br />should be at least $400
					(last bet : $200 + last raise $200)<br />=> replaced by <b>BET
						400</b>
				</li>
				<li>player 2 : <b>BET 500</b><br />should be at least $600
					(last bet : $400 + last raise $200)<br />=> replaced by <b>BET
						600</b>
				</li>
			</ol>
		</td>
	</tr>
	<tr>
		<td  style="border: 2px solid" align="center"><b>ALL-IN</b></td>
		</td>
		<td  style="border: 2px solid" align="left">FLOP
			<ol>
				<li>player 0 : <b>BET 200</b></li>
				<li>player 1 : <b>BET 300</b> with a stack of $350 (should be
					at least $400)<br />=> replaced by <b>ALL-IN</b>
				</li>
			</ol>
		</td>
	</tr>
	<tr>
		<td  style="border: 2px solid" align="center"><b>CHECK</b></td>
		<td  style="border: 2px solid" align="center"><b>FOLD</b></td>
		<td  style="border: 2px solid" align="left">Player must add chips to the pot (<b>CALL</b> or
			<b>BET</b>).
		</td>
		<td  style="border: 2px solid" align="left">FLOP
			<ol>
				<li>player 0 : <b>BET 200</b></li>
				<li>player 1 : <b>CHECK</b> (impossible)</li>
			</ol>
		</td>
	</tr>
	<tr>
		<td  style="border: 2px solid" align="center"><b>INVALID ACTION</b></td>
		<td  style="border: 2px solid" align="center"><b>FOLD</b></td>
		<td  style="border: 2px solid" align="left">Invalid action</td>
		<td  style="border: 2px solid" align="left">
			<ol>
				<li>player 0 : <b>RAISE 200</b> (RAISE is invalid)
				</li>
				<li>player 0 : <b>TOTO</b> (invalid)
				</li>
				<li>player 0 : <b>BET $200</b> (invalid)
				</li>
				<li>player 0 : <b>BET 0</b> (invalid amount)
				</li>
				<li>player 0 : <b>BET -20</b> (invalid amount)
				</li>
				<li>player 0 : <b>CALL 10</b> (invalid)
				</li>
			</ol>
		</td>
	</tr>

</table>

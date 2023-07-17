<h1>ACTION REPLACEMENT</h1>
<!--<style>
table, td, th {
	border: 1px solid #000;
	border-collapse: collapse;
}
</style>-->
<table  border="1px solid #000" cellspacing="0" cellpadding="0">
	<tr height="80">
		<th align="center">player action</th>
		<th align="center">replacement action</th>
		<th align="center">explanation</th>
		<th align="center">example</th>
	</tr>
		<tr>
		<td align="left" colspan="4" height="80"><u><b style="color:red">WARNING</b></u> : <b>FOLD</b> is never replaced. Even if a <b>CHECK</b> is possible.
		</td>
	</tr>
	<tr>
		<td align="center"><b>CALL</b></td>
		<td align="center"><b>ALL-IN</b></td>
		<td align="left">Player hasn't enough chips to <b>CALL</b>.
		</td>
		<td align="left">POST-FLOP
			<ol>
				<li>player 0 : <b>BET 200</b></li>
				<li>player 1 : <b>CALL</b> (with a stack of $100)
				</li>
			</ol>
		</td>
	</tr>
	<tr>
		<td align="center"><b>CALL</b></td>
		<td align="center"><b>CHECK</b></td>
		<td align="left">Player can only <b>CHECK</b> or <b>RAISE</b>.
		</td>
		<td align="left">PRE-FLOP with a big blind of $10
			<ol>
				<li>player 1 : <b>CALL</b> $5
				</li>
				<li>player 2 : <b>CALL</b> $5
				</li>
				<li>player 0 (big blind) : can only <b>CHECK</b> or <b>RAISE</b></li>
			</ol>
		</td>
	</tr>
	<tr>
		<td align="center"><b>BET</b> / <b>ALL-IN</b></td>
		<td align="center"><b>CALL</b></td>
		<td align="left">Player can only <b>CALL</b> or <b>FOLD</b>,
			since he was<br /> <b>the last one to make a 'proper' raise</b>.
		</td>
		<td align="left">POST-FLOP
			<ol>
				<li>player 0 : <b>BET 200</b></li>
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
		<td align="center"><b>BET</b></td>
		<td align="center"><b>ALL-IN</b></td>
		<td align="left">Player hasn't enough chips to bet the given
			amount.</td>
		<td align="left">POST-FLOP
			<ol>
				<li>player 0 : <b>BET 200</b> (with a stack of $100)
				</li>
			</ol>
		</td>
	</tr>
	<tr>
		<td align="left" colspan="4" height="80">The basic <b>BET</b>
			rule is : if you put chip in the pot you <b style="color:red">can't take them back</b>.You
			have to complete the bet so it's legal.
		</td>
	</tr>
	<tr>
		<td align="center" rowspan="2"><b>BET</b></td>
		<td align="center"><b>BET</b></td>
		<td align="left" rowspan="2">The bet amount must <b>at least
				be one big blind</b>.
		</td>
		<td align="left">POST-FLOP with a big blind of $10
			<ol>
				<li>player 0 : <b>BET 5</b> (should be at least $10)<br />=>
					replaced by <b>BET 10</b>
				</li>
			</ol>
		</td>
	</tr>
	<tr>
		<td align="center"><b>ALL-IN</b></td>
		<td align="left">POST-FLOP with a big blind of $10
			<ol>
				<li>player 0 : <b>BET 5</b> with a stack of $7 (should be at
					least $10)<br />=> replaced by <b>ALL-IN</b>
				</li>
			</ol>
		</td>
	</tr>
	<tr>
		<td align="center" rowspan="2"><b>BET</b></td>
		<td align="center"><b>BET</b></td>
		<td align="left" rowspan="2">The bet amount must <b>at least
				be the last raise</b>.
		</td>
		<td align="left">POST-FLOP
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
		<td align="center"><b>ALL-IN</b></td>
		</td>
		<td align="left">POST-FLOP
			<ol>
				<li>player 0 : <b>BET 200</b></li>
				<li>player 1 : <b>BET 150</b> with a stack of $180 (should be
					at least $400)<br />=> replaced by <b>ALL-IN</b>
				</li>
			</ol>
		</td>
	</tr>
	<tr>
		<td align="center"><b>CHECK</b></td>
		<td align="center"><b>FOLD</b></td>
		<td align="left">Player must add chips to the pot (<b>CALL</b> or
			<b>BET</b>).
		</td>
		<td align="left">POST-FLOP
			<ol>
				<li>player 0 : <b>BET 200</b></li>
				<li>player 1 : <b>CHECK</b> (impossible)
				</li>
			</ol>
		</td>
	</tr>
	<tr>
		<td align="center"><b>INVALID ACTION</b></td>
		<td align="center"><b>FOLD</b></td>
		<td align="left">Invalid action</td>
		<td align="left">
			<ol>
				<li>player 0 : <b>RAISE 200</b> (RAISE is invalid)
				</li>
				<li>player 1 : <b>TOTO</b> (invalid)
				</li>
				<li>player 2 : <b>BET $200</b> (invalid)
				</li>
			</ol>
		</td>
	</tr>

</table>

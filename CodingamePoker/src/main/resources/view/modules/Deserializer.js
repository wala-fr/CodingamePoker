function parseIntList(values) {
	const ret = [];
	for (let i = 0; i < values.length; i++) {
		ret.push(+values[i]);
	}
	return ret;
}
export function parseGlobalData(raw) {
	const data = {};
	let idx = 0;
	// console.error(raw)
	const lines = raw.split('\n').map(line => line === '' ? [] : line.split(' '));
	data.cardIds = parseIntList(lines[idx++]);
	data.debugCardIds = parseIntList(lines[idx++]);
	data.showOpponentCardIds = parseIntList(lines[idx++]);
	if (idx < lines.length) {
		data.winTextIds = parseIntList(lines[idx++]);
	}
	return data;
}
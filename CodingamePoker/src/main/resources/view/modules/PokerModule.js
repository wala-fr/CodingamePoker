import { parseGlobalData } from './Deserializer.js';
import { GraphicEntityModule, api } from '../entity-module/GraphicEntityModule.js';

/* global PIXI */
const apiPoker = {
	toPixel: 1,
	globalData: {},
	options: {
		showOpponentCards: false,
		debugMode: false,
		meInGame: false,
		enableHideOpponentCards: true,
		showPercentTime: false,
		showFoldCards: false,
	},
	showOpponentCards: () => {
		showHide2(apiPoker.globalData.showOpponentCardIds)
		showHide(apiPoker.globalData.showPlayerCardIds, false)
		const showOpponentCards = apiPoker.options.showOpponentCards;
		let winTextIds = apiPoker.globalData.winTextIds;
		if (winTextIds != undefined) {
			// the last winTextIds corresponds to the tie common group
			// [winTextId0, winTextHideId0, winTextId1, winTextHideId1, ..., tiegoupId]
			for (let i = 0; i < winTextIds.length; i++) {
				api.entities.get(winTextIds[i++]).setHidden(i % 2 == 0 ? showOpponentCards : !showOpponentCards);
			}
		}
	},
	setDebug: () => {
		showHide2(apiPoker.globalData.cardIds)
		showHide2(apiPoker.globalData.debugCardIds)
	},
	showPercentTime: () => {
		showHide(apiPoker.globalData.percentTimeIds, apiPoker.options.showPercentTime)
	},
	showFoldCards: () => {
		showHide2(apiPoker.globalData.foldCardIds)
	}
};

function isVisible(cardId) {
	let ret = true;
	if (apiPoker.globalData.foldCardIds.includes(cardId)) {
		ret = apiPoker.options.showFoldCards;
	}
	if (ret) {
		if (apiPoker.globalData.cardIds.includes(cardId)) {
			ret = !apiPoker.options.debugMode;
		} else if (apiPoker.globalData.debugCardIds.includes(cardId)) {
			ret = apiPoker.options.debugMode;
		}
	}
	if (ret && apiPoker.globalData.showOpponentCardIds.includes(cardId)) {
		ret = !apiPoker.options.showOpponentCards
	}
	return ret;
}

function showHide2(cardIds) {
	if (cardIds === undefined || api.entities === undefined) {
		return;
	}
	for (let i = 0; i < cardIds.length; i++) {
		let cardId = cardIds[i];
		api.entities.get(cardId).setHidden(!isVisible(cardId));
	}
}

function showHide(cardIds, show) {
	if (cardIds === undefined || api.entities === undefined) {
		return;
	}
	for (let i = 0; i < cardIds.length; i++) {
		api.entities.get(cardIds[i]).setHidden(!show);
		//	console.error(i + " " + cardIds[i] +" " + api.entities.get(cardIds[i]).container.visible);
	}
}

function update() {
	apiPoker.setDebug()
	apiPoker.showOpponentCards()
	apiPoker.showPercentTime()
	apiPoker.showFoldCards()
}

// to test locally where there's no isMe
const debug = false;
function filter(player) { return debug ? player.index == 0 : player.isMe }
function addShowCard(player, globalData, array, players) {
	const varNb = globalData.showOpponentCardIds.length / players.length;
	for (let i = 0; i < varNb; i++) {
		array.push(globalData.showOpponentCardIds[player.index * varNb + i])
	}
}

export { apiPoker };
export class PokerModule {
	static get moduleName() {
		return 'poker';
	}

	handleFrameData(frameInfo, frameData) {
		return { frameInfo, frameData }
	}

	handleGlobalData(players, data) {
		this.globalData = {
			players: players,
			playerCount: players.length
		}
		if (data !== undefined) {
			const parsedData = parseGlobalData(data);
			this.globalData.cardIds = parsedData.cardIds;
			this.globalData.debugCardIds = parsedData.debugCardIds;
			this.globalData.winTextIds = parsedData.winTextIds;
			this.globalData.percentTimeIds = parsedData.percentTimeIds;
			this.globalData.foldCardIds = parsedData.foldCardIds;

			const showOpponentCardIds = [];
			players.filter(p => !filter(p)).map(p => { addShowCard(p, parsedData, showOpponentCardIds, players) })
			this.globalData.showOpponentCardIds = showOpponentCardIds;

			// TODO USEFULL
			const showPlayerCardIds = [];
			players.filter(p => filter(p)).map(p => { addShowCard(p, parsedData, showPlayerCardIds, players) })
			this.globalData.showPlayerCardIds = showPlayerCardIds;

		}
		apiPoker.globalData = this.globalData;
		apiPoker.options.meInGame = !!players.find(p => p.isMe);
		if (!apiPoker.options.meInGame && !debug) {
			apiPoker.options.enableHideOpponentCards = false;
			apiPoker.globalData.showOpponentCards = true;
		} else {
			apiPoker.options.enableHideOpponentCards = true;
		}
	}

	updateScene(previousData, currentData, progress, speed) {
		update()
	}

	reinitScene(container, canvasData) {
	}

	animateScene(delta) {
	}
}

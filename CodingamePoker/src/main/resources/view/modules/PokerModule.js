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
		enableHideOpponentCards: true
	},
	showOpponentCards: () => {
		let cardIds = apiPoker.globalData.showOpponentCardIds;
		if (cardIds === undefined || api.entities === undefined) {
			return;
		}
		const showOpponentCards = apiPoker.options.showOpponentCards;
		for (let i = 0; i < cardIds.length; i++) {
			api.entities.get(cardIds[i]).setHidden(showOpponentCards);
		}
		cardIds = apiPoker.globalData.showPlayerCardIds;
		for (let i = 0; i < cardIds.length; i++) {
			api.entities.get(cardIds[i]).setHidden(true);
		}
	},
	setDebug: () => {
		const cardIds = apiPoker.globalData.cardIds;
		if (cardIds === undefined || api.entities === undefined) {
			return;
		}
		const debugCardIds = apiPoker.globalData.debugCardIds;
		const debugMode = apiPoker.options.debugMode;
		for (let i = 0; i < cardIds.length; i++) {
			api.entities.get(cardIds[i]).setHidden(debugMode);
			api.entities.get(debugCardIds[i]).setHidden(!debugMode);
		}
	},
};

// to test locally where there's no isMe
const debug = false;
function filter(player) { return debug ? player.index == 0 : player.isMe }
function addShowCard(player, globalData, array) { array.push(globalData.showOpponentCardIds[player.index * 2]), array.push(globalData.showOpponentCardIds[player.index * 2 + 1]) }

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
			const globalData = parseGlobalData(data);
			this.globalData.cardIds = globalData.cardIds;
			this.globalData.debugCardIds = globalData.debugCardIds;
			const showOpponentCardIds = [];
			players.filter(p => !filter(p)).map(p => { addShowCard(p, globalData, showOpponentCardIds) })
			this.globalData.showOpponentCardIds = showOpponentCardIds;

			const showPlayerCardIds = [];
			players.filter(p => filter(p)).map(p => { addShowCard(p, globalData, showPlayerCardIds) })
			this.globalData.showPlayerCardIds = showPlayerCardIds;

		}
		apiPoker.globalData = this.globalData;
		apiPoker.options.meInGame = !!players.find(p => p.isMe);
		if (!apiPoker.options.meInGame && !debug) {
			apiPoker.options.enableHideOpponentCards = false;
			apiPoker.globalData.showPlayerCardIds = true;
		} else {
			apiPoker.options.enableHideOpponentCards = true;
		}
	}


	updateScene(previousData, currentData, progress, speed) {
		apiPoker.setDebug()
		apiPoker.showOpponentCards()
	}

	reinitScene(container, canvasData) {
	}

	animateScene(delta) {
	}
}

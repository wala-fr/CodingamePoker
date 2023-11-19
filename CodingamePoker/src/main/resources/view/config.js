import { PokerModule, apiPoker } from './modules/PokerModule.js'
import { GraphicEntityModule } from './entity-module/GraphicEntityModule.js';
import { TooltipModule } from './tooltip-module/TooltipModule.js';
import { ToggleModule } from './toggle-module/ToggleModule.js'
import { EndScreenModule } from './endscreen-module/EndScreenModule.js';

// List of viewer modules that you want to use in your game
export const modules = [
	GraphicEntityModule,
	TooltipModule,
	ToggleModule,
	EndScreenModule,
	PokerModule
];

export const gameName = 'Poker'

const opponentCardTitle = 'OPPONENTS CARDS'
function findOption(title) {
	for (let c of options) {
		if (c.title == title) {
			return c;
		}
	}
}

export const options = [{
	title: 'DEBUG',
	get: function() {
		return apiPoker.options.debugMode
	},
	set: function(value) {
		apiPoker.options.debugMode = value
		apiPoker.setDebug()
	},
	values: {
		'ON': true,
		'OFF': false
	},
	default: false
}, {
	title: opponentCardTitle,
	get: function() {
		return apiPoker.options.showOpponentCards
	},
	set: function(value) {
		apiPoker.options.showOpponentCards = value
		apiPoker.showOpponentCards();
	},
	enabled: function() {
		const ret = apiPoker.options.enableHideOpponentCards
		if (!ret) {
			findOption(opponentCardTitle).set(true)
		}
		return ret
	},
	values: {
		'SHOW': true,
		'HIDE': false
	},
	default: false
/*}, {
	title: 'PERCENT TIME',
	get: function() {
		return apiPoker.options.showPercentTime
	},
	set: function(value) {
		apiPoker.options.showPercentTime = value
		apiPoker.showPercentTime();
	},
	values: {
		'SHOW': true,
		'HIDE': false
	},
	default: false*/
},]


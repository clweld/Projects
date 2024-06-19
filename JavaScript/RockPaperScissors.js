const readline = require('readline-sync');
const VALID_CHOICES = ['rock', 'paper', 'scissors', 'lizard', 'spock'];
const VALID_REPLAY_ANSWERS = ['yes', 'no', 'y', 'n'];
const WINNING_PLAYS = {
  scissors: ['paper', 'lizard'],
  paper: ['rock', 'spock'],
  rock: ['lizard', 'scissors'],
  lizard: ['spock', 'paper'],
  spock: ['scissors', 'rock']
};
const OUTCOME = ['win', 'lose', 'tie'];

function prompt(message) {
  console.log(`=> ${message}`);
}

function clearConsole() {
  console.clear();
}

function welcome() {
  prompt('Welcome!');
}

function startGame() {
  prompt("Let's play a game of Rock, Paper, Scissors, Lizard, Spock!");
}

function displayRules() {
  prompt('You vs Computer: first to 3 points wins.');
}

function lineBreak() {
  console.log('----------------------------------------------------');
}

function resetGame() {
  userScore = 0;
  compScore = 0;
}

function getUserChoice() {
  prompt(`Choose one: ${VALID_CHOICES.join(', ')}.` +
  '\n You may type: "r" or rock, "p" or paper, "sc" or scissors, "l" or lizard,  "sp" or spock.');
  let userChoice = readline.question().toLowerCase();
  return userChoice;
}

function shortInput(userChoice) {
  switch (userChoice) {
    case 'r':
      return 'rock';
    case 'p':
      return 'paper';
    case 'sc':
      return 'scissors';
    case 'l':
      return 'lizard';
    case 'sp':
      return 'spock';
  }
}

function invalidChoice(userChoice) {
  while (!VALID_CHOICES.includes(userChoice)) {
    prompt('That is not a valid choice, try again.');
    userChoice = readline.question().toLowerCase();
    userChoice = shortInput(userChoice);
  }
  return userChoice;
}

function getCompChoice() {
  let randomIndex = Math.floor(Math.random() * VALID_CHOICES.length);
  let computerChoice = VALID_CHOICES[randomIndex];
  return computerChoice;
}

function displayChoices(userChoice, computerChoice) {
  prompt(`You chose ${userChoice} and the computer chose ${computerChoice}.`);
}

function determineOutcome(userChoice, computerChoice) {
  if (WINNING_PLAYS[userChoice].includes(computerChoice)) {
    return OUTCOME[0];
  } else if (WINNING_PLAYS[computerChoice].includes(userChoice)) {
    return OUTCOME[1];
  } else {
    return OUTCOME[2];
  }
}

function displayOutcome(outcome) {
  if (outcome === OUTCOME[0]) {
    prompt(`You ${OUTCOME[0]}! :)`);
  } else if (outcome === OUTCOME[1]) {
    prompt(`You ${OUTCOME[1]}! :(`);
  } else {
    prompt(`It's a ${OUTCOME[2]}! :|`);
  }
}

function incrementScore(outcome) {
  if (outcome === OUTCOME[0]) {
    userScore += 1;
  } else if (outcome === OUTCOME[1]) {
    compScore += 1;
  }
}

function displayScore(userScore, compScore) {
  prompt(`Your score: ${userScore} points\nComputer score: ${compScore} points`);
}

function displayWinner(userScore, compScore, winningScore) {
  if (userScore === winningScore) {
    prompt('You win the game!');
  } else if (compScore === winningScore) {
    prompt('Computer wins the game!');
  }
  return true;
}

function keepPlaying(userScore, compScore, winningScore) {
  if (userScore < winningScore && compScore < winningScore) {
    return true;
  }
}

function getPlayAgain() {
  prompt('Would you like to play again? (yes/no) or (y/n)');
  let answer = readline.question().toLowerCase();
  return answer;
}

function invalidPlayAgain(answer) {
  while (!VALID_REPLAY_ANSWERS.includes(answer)) {
    prompt('Please enter a valid response.');
    answer = readline.question().toLowerCase();
  }
  return answer;
}

function endGame(answer) {
  if (answer === 'n' || answer === 'no') {
    prompt('Thanks for playing!');
    return true;
  }
}


// Start Program

let winningScore = 3;
let userScore = 0;
let compScore = 0;

while (true) {

  clearConsole();

  welcome();

  startGame();

  displayRules();

  lineBreak();

  resetGame();

  while (keepPlaying(userScore, compScore, winningScore)) {

    let userChoice = getUserChoice();

    userChoice = invalidChoice(shortInput(userChoice));

    let computerChoice = getCompChoice();

    displayChoices(userChoice, computerChoice);

    let outcome = determineOutcome(userChoice, computerChoice);

    displayOutcome(outcome);

    incrementScore(outcome);

    displayScore(userScore, compScore);

    lineBreak();
  }

  displayWinner(userScore, compScore, winningScore);

  let answer = getPlayAgain();

  invalidPlayAgain(answer);

  if (endGame(answer)) {
    break;
  }
}
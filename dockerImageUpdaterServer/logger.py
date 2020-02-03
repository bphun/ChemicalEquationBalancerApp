import logging, sys

BLACK, RED, GREEN, YELLOW, BLUE, MAGENTA, CYAN, WHITE = range(8)

# The background is set with 40 plus the number of the color, and the foreground with 30

# These are the sequences need to get colored ouput
RESET_SEQ = "\033[0m"
COLOR_SEQ = "\033[1;%dm"
BOLD_SEQ = "\033[1m"

COLORS = {
    'WARNING': YELLOW,
    'INFO': WHITE,
    'DEBUG': BLUE,
    'CRITICAL': YELLOW,
    'ERROR': RED
}

def replaceColorTags(str, shouldColorize = True):
    if shouldColorize:
        return str.replace("$RESET", RESET_SEQ).replace("$BOLD", BOLD_SEQ)
    else:
        return str.replace("$RESET", "").replace("$BOLD", "")

class ConsoleColorFormatter(logging.Formatter):
    def __init__(self, msg, useColor = True):
        logging.Formatter.__init__(self, msg)
        self.useColor = useColor

    def format(self, record):
        levelname = record.levelname
        if self.useColor and levelname in COLORS:
            colorizedLevelname = COLOR_SEQ % (30 + COLORS[levelname]) + levelname
            record.levelname = colorizedLevelname
        return logging.Formatter.format(self, record)

class Logger(logging.Logger):

    def __init__(self,logFilePath, formatStr=None):
        super().__init__(self, logging.DEBUG)
        formatString = formatStr or "%(asctime)s [%(processName)-13.13s] [%(threadName)-10.10s] [$BOLD%(levelname)-12.12s$RESET]  %(message)s"

        logFormatter = logging.Formatter(formatString)

        parsedColorizedString = replaceColorTags(formatString)
        consoleColorFormatter = ConsoleColorFormatter(parsedColorizedString)

        self.setLevel(logging.DEBUG)

        self.fileHandler = logging.FileHandler(logFilePath)
        self.fileHandler.setFormatter(logFormatter)

        self.consoleHandler = logging.StreamHandler(sys.stdout)
        self.consoleHandler.setFormatter(consoleColorFormatter)
        self.consoleHandler.setLevel(logging.DEBUG)

        self.addHandler(self.fileHandler)
        self.addHandler(self.consoleHandler)

    def getFileHandler(self):
        return self.fileHandler

    def getConsoleHandler(self):
        return self.consoleHandler


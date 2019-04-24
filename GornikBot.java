package bots;

import arena.BattleBotArena;
import arena.BotInfo;
import arena.Bullet;

import java.awt.*;

public class GornikBot extends Bot {
    // creating a bot helper object for later assistance
    BotHelper pikachuHelper = new BotHelper();

    // variable to hold distance
    private static int disDanger = 60;

    // variable to hold shooting range
    private static int disShooting = 70;

    // variable to hold if a bullet is close
    private boolean bulletClose;

    /**
     * This method is called at the beginning of each round. Use it to perform
     * any initialization that you require when starting a new round.
     */
    @Override
    public void newRound() {

    }

    /**
     * This method is called at every time step to find out what you want your
     * Bot to do. The legal moves are defined in constants in the BattleBotArena
     * class (UP, DOWN, LEFT, RIGHT, FIREUP, FIREDOWN, FIRELEFT, FIRERIGHT, STAY,
     * SEND_MESSAGE). <br><br>
     * <p>
     * The <b>FIRE</b> moves cause a bullet to be created (if there are
     * not too many of your bullets on the screen at the moment). Each bullet
     * moves at speed set by the BULLET_SPEED constant in BattleBotArena. <br><br>
     * <p>
     * The <b>UP</b>, <b>DOWN</b>, <b>LEFT</b>, and <b>RIGHT</b> moves cause the
     * bot to move BOT_SPEED
     * pixels in the requested direction (BOT_SPEED is a constant in
     * BattleBotArena). However, if this would cause a
     * collision with any live or dead bot, or would move the Bot outside the
     * playing area defined by TOP_EDGE, BOTTOM_EDGE, LEFT_EDGE, and RIGHT_EDGE,
     * the move will not be allowed by the Arena.<br><Br>
     * <p>
     * The <b>SEND_MESSAGE</b> move (if allowed by the Arena) will cause a call-back
     * to this Bot's <i>outgoingMessage()</i> method, which should return the message
     * you want the Bot to broadcast. This will be followed with a call to
     * <i>incomingMessage(String)</i> which will be the echo of the broadcast message
     * coming back to the Bot.
     *
     * @param me       A BotInfo object with all publicly available info about this Bot
     * @param shotOK   True iff a FIRE move is currently allowed
     * @param liveBots An array of BotInfo objects for the other Bots currently in play
     * @param deadBots An array of BotInfo objects for the dead Bots littering the arena
     * @param bullets  An array of all Bullet objects currently in play
     * @return A legal move (use the constants defined in BattleBotArena)
     */

//    Can be later replaced.
    /*
        for(Bullet bullet: bullets) {

        }
        */
    @Override
    public int getMove(BotInfo me, boolean shotOK, BotInfo[] liveBots, BotInfo[] deadBots, Bullet[] bullets) {
        bulletClose = false;

        // finding the closest bot to me
        BotInfo closestBot = pikachuHelper.findClosest(me, liveBots);
        //Bullet bullet = pikachuHelper.findClosest(me, bullets);

        // looping through all bullets in the world (I prefer to use this in case 2+ bullets are approaching the bot)
        for (Bullet bullet : bullets) {
            // trying to dodge when a bullet is shot and is within the danger distance
            if (pikachuHelper.calcDistance(me.getX(), me.getY(), bullet.getX(), bullet.getY()) <= disDanger) {
                bulletClose = true;

                    // bullet approaching from right or left (JUMPS TOO LATE)
                    if (bullet.getX() >= me.getX() + Bot.RADIUS || bullet.getX() + Bot.RADIUS <= me.getX()) {
                        System.out.println("BULLET: " + bullet);
                        System.out.println("1. BULLET APPROACHING FROM RIGHT OR LEFT");

                        // ensuring that only bullets that move horizontally are targeted
                        if (bullet.getXSpeed() != 0 && bullet.getYSpeed() == 0) {

                            // if me is above bullet
                            if (me.getY() + Bot.RADIUS > bullet.getY()) {
                                System.out.println("1. DOWN");
                                return BattleBotArena.DOWN;
                            } else {
                                System.out.println("1. UP");
                                return BattleBotArena.UP;
                            }
                        }
                    }

                // if bot is at the edge of the screen (left/right edge)
                if (bullet.getY() >= me.getY() + Bot.RADIUS || bullet.getY() + Bot.RADIUS <= me.getY()) {
                    //System.out.println("2. BULLET APPROACHING FROM UP OR DOWN");

                    // making sure bullet is being shot vertically
                    if (bullet.getYSpeed() != 0 && bullet.getXSpeed() == 0) {

                        // if approaching from above and to my right (moving in opp direction)
                        if (me.getX() + Bot.RADIUS > bullet.getX()) {
                            System.out.println("****");
                            return BattleBotArena.RIGHT;
                        } else {
                            return BattleBotArena.LEFT;
                        }
                    }
                }
            }

        }


        /*
        // checking if i bump into another bot and move away
        if(pikachuHelper.calcDistance(me.getX(), me.getY(), closestBot.getX(), closestBot.getY()) <= Bot.RADIUS*2) {
            System.out.println("Bumping into bot");

            // checking from what direction i bump into the bot+
        }
        */

        // Chasing other bots
        double dispX = pikachuHelper.calcDisplacement(me.getX() + Bot.RADIUS, closestBot.getX());
        double dispY = pikachuHelper.calcDisplacement(me.getY() + Bot.RADIUS, closestBot.getY());
        double distanceFromBot = pikachuHelper.calcDistance(me.getX() + Bot.RADIUS, me.getY() +
                Bot.RADIUS, closestBot.getX(), closestBot.getY());

        System.out.println("Distance is: " + distanceFromBot);
        //System.out.println("My Y: " + me.getY() + " BattleBot: " + closestBot.getY());

        // while NOT at a distance of 50 from the bot // Note: liveBots is always the same even after overheating
        // which causes my bot to constantly move back and forth (unstable)
        if (distanceFromBot > 80 && !bulletClose && liveBots.length != 0) {
            System.out.println("Live bots: " + liveBots.length + " Dead: " + deadBots.length);

            // if my bot is NOT aligned with the battle bot (not accurate since the bot is always moving therefore I use range)
            if (!(me.getY() >= closestBot.getY() - 15
                    && me.getY() <= closestBot.getY() + 15)) {

                if (dispY > 0) {
                    System.out.println("Need to move DOWN");
                    return BattleBotArena.DOWN;

                } else if (dispY < 0) {
                    System.out.println("Need to move UP");
                    return BattleBotArena.UP;
                }
            } else {
                if (dispX > 0) {
                    System.out.println("Need to move right");
                    return BattleBotArena.RIGHT;

                } else if (dispX < 0) {
                    System.out.println("Need to move left");
                    return BattleBotArena.LEFT;

                } else {
                    return 0;
                }
            }
        }

        // shooting occurs here once the required distance has met
        else {
            // for accuracy purposes, can add me.getY() >= closestBot.getY() - 15 && me.getY() <= closestBot.getY() + 15
            if (!bulletClose) {
                // Killing bots near my range
                if (pikachuHelper.calcDistance(me.getX(), me.getY(), closestBot.getX(), closestBot.getY()) <= disShooting) {
                    // checking from what direction i face the bot
                    // NOTE: use range to check whether the bot is above/below/front/behind+
                    if (shotOK && me.getX() + Bot.RADIUS > closestBot.getX()) { // farther apart from the bot
                        System.out.println("He's to my left");
                        return BattleBotArena.FIRELEFT;

                    } else if (shotOK && me.getX() + Bot.RADIUS < closestBot.getX()) {
                        System.out.println("He's to my right");
                        return BattleBotArena.FIRERIGHT;


                    } else if (shotOK && me.getY() + Bot.RADIUS > closestBot.getY()) {
                        System.out.println("He's below me");
                        return BattleBotArena.FIREDOWN;

                    } else if (shotOK && me.getY() + Bot.RADIUS < closestBot.getY()) {
                        System.out.println("He's above me");
                        return BattleBotArena.FIREUP;
                    }
                }
            }
        }

        // if my bot is stuck either at the edge of the screen or to a different bot
        // Moving bot if trapped at either the left or right edges of the screen [NEED TO BE FIXED]
        if (!bulletClose) {
            if (me.getX() == BattleBotArena.LEFT_EDGE) {
                return BattleBotArena.RIGHT;
            } else if (me.getX() == BattleBotArena.RIGHT_EDGE) {
                return BattleBotArena.LEFT;
            }

            for (BotInfo stuckBot : liveBots) {
                System.out.println("D: " + pikachuHelper.calcDistance(me.getX(), me.getY(), stuckBot.getX(), stuckBot.getY()));
                // overlapping with a different bot
                if (pikachuHelper.calcDistance(me.getX(), me.getY(), stuckBot.getX(), stuckBot.getY()) <= Bot.RADIUS * 2) {
                    System.out.println("STUCK!");
                    // checking what direction the bot is at
                    if (me.getX() + Bot.RADIUS > stuckBot.getX()) {
                        // if bot is on my left
                        return BattleBotArena.RIGHT;
                    } else {
                        return BattleBotArena.LEFT;
                    }
                }
            }
        }

        // follow closest bot and shoot when at a certain range from bot
//            //double disFromBot = pikachuHelper.calcDisplacement(me.getX(), closestBot.getX());
//            if(me.getX() + Bot.RADIUS > disFromBot) {
//                // determining what direction do I face the bot from
////                System.out.println("I am close! Distance: " + disFromBot);
//            }
//        }

//        // checking if my bot is close to any other bot
//        for (int j = 0; j < liveBots.length; j++) {
//            if (pikachuHelper.calcDistance(me.getX(), me.getY(), closestBot.getX(), closestBot.getY()) >= disDanger) {
//                // bullet approaching from left
//                if (closestBot.getX() > me.getX()) {
//                    // if not at the edge of the screen
//                    if (me.getY() < BattleBotArena.HEIGHT) {
//                        //System.out.println("NOT AT TOP, UPP!");
//                        return BattleBotArena.UP;
//                    } else {
//                        //System.out.println("YES AT TOP, DOWNNN!");
//                        return BattleBotArena.DOWN;
//                    }
//                }
//                // bullet approaching from right
//                else if (closestBot.getX() < me.getX()) {
//                    // if not at the edge of the screen
//                    if (me.getY() < BattleBotArena.HEIGHT) {
////                        System.out.println("NOT AT TOP, UPP!");
//                        return BattleBotArena.UP;
//                    } else {
////                        System.out.println("YES AT TOP, DOWNNN!");
//                        return BattleBotArena.DOWN;
//                    }
//                }
//                // bullet approaching from up
//                else if (closestBot.getY() > me.getY()) {
//                    return BattleBotArena.RIGHT;
//
//                }
//                // bullet approaching from down
//                else if (closestBot.getY() < me.getY()) {
//                    return BattleBotArena.LEFT;
//                }
//
//            }
//        }
//
//        return BattleBotArena.RIGHT;
        return 0;
    }

    /**
     * Called when it is time to draw the Bot. Your Bot should be (mostly)
     * within a circle inscribed inside a square with top left coordinates
     * <i>(x,y)</i> and a size of <i>RADIUS * 2</i>. If you are using an image,
     * just put <i>null</i> for the ImageObserver - the arena has some special features
     * to make sure your images are loaded before you will use them.
     *
     * @param g The Graphics object to draw yourself on.
     * @param x The x location of the top left corner of the drawing area
     * @param y The y location of the top left corner of the drawing area
     */
    @Override
    public void draw(Graphics g, int x, int y) {
        g.setColor(Color.yellow);
        g.fillRect(x + 2, y + 2, RADIUS * 2 - 4, RADIUS * 2 - 4);
    }

    /**
     * This method will only be called once, just after your Bot is created,
     * to set your name permanently for the entire match.
     *
     * @return The Bot's name
     */
    @Override
    public String getName() {
        return null;
    }

    /**
     * This method is called at every time step to find out what team you are
     * currently on. Of course, there can only be one winner, but you can
     * declare and change team allegiances throughout the match if you think
     * anybody will care. Perhaps you can send coded broadcast message or
     * invitation to other Bots to set up a temporary team...
     *
     * @return The Bot's current team name
     */
    @Override
    public String getTeamName() {
        return null;
    }

    /**
     * This is only called after you have requested a SEND_MESSAGE move (see
     * the documentation for <i>getMove()</i>). However if you are already over
     * your messaging cap, this method will not be called. Messages longer than
     * 200 characters will be truncated by the arena before being broadcast, and
     * messages will be further truncated to fit on the message area of the screen.
     *
     * @return The message you want to broadcast
     */
    @Override
    public String outgoingMessage() {
        return null;
    }

    /**
     * This is called whenever the referee or a Bot sends a broadcast message.
     *
     * @param botNum The ID of the Bot who sent the message, or <i>BattleBotArena.SYSTEM_MSG</i> if the message is from the referee.
     * @param msg    The text of the message that was broadcast.
     */
    @Override
    public void incomingMessage(int botNum, String msg) {

    }

    /**
     * This is called by the arena at startup to find out what image names you
     * want it to load for you. All images must be stored in the <i>images</i>
     * folder of the project, but you only have to return their names (not
     * their paths).<br><br>
     * <p>
     * PLEASE resize your images in an image manipulation
     * program. They should be squares of size RADIUS * 2 so that they don't
     * take up much memory.
     *
     * @return An array of image names you want the arena to load.
     */
    @Override
    public String[] imageNames() {
        return new String[0];
    }

    /**
     * Once the arena has loaded your images (see <i>imageNames()</i>), it
     * calls this method to pass you the images it has loaded for you. Store
     * them and use them in your draw method.<br><br>
     * <p>
     * PLEASE resize your images in an
     * image manipulation program. They should be squares of size RADIUS * 2 so
     * that they don't take up much memory.<br><br>
     * <p>
     * CAREFUL: If you got the file names wrong, the image array might be null
     * or contain null elements.
     *
     * @param images The array of images (or null if there was a problem)
     */
    @Override
    public void loadedImages(Image[] images) {

    }
}

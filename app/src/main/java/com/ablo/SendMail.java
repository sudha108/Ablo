package com.ablo;

/**
 * Created by sudhams on 20/02/18.
 */

public class SendMail {

    public SendMail(final String username,final String password,final String mailid){
        new Thread(new Runnable() {

            public void run() {

                try {

                    Mail sender = new Mail(

                            "projectsensu@gmail.com",

                            "Project/283");


                    sender.sendMail("Welcome to Ablo",
                            "\nHi,\n\nIt is our great privilege to inform you that you have been added as a User " +
                                    "to Ablo by your Admin." +
                                    "\n\nYour username is :\t" +username +
                                    "\nYour password is :\t" + password +
                                    "\n\n Please cooperate and download Ablo. Login with above mentioned username and " +
                                    "password to help your organisation improve"+
                                    "\n\n P.S: We will always know if you disobey"+
                                    "\n\n\n Thank You",

                            "Ablo",  mailid);

                } catch (Exception e) {

                    e.printStackTrace();

                }

            }

        }).start();
    }
}

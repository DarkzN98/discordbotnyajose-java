package com.github.darkzn98;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.MessageDecoration;
import org.javacord.api.entity.message.embed.EmbedBuilder;

import javax.imageio.ImageIO;
import java.io.File;
import java.net.URLConnection;
import java.net.URL;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        String token = "NDk5MjY0NTQwMDY4ODA2NjY3.Dp5wgg.aScYlvxFHSpC6O8GmyETHJ2oC4E";
        String prefix = ";";
        DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();

        //addListener
        api.addMessageCreateListener(event ->
        {
            if(event.getMessage().getContent().equalsIgnoreCase(prefix +"Ping"))
            {
                event.getChannel().sendMessage("pong!");
            }
            else if(event.getMessage().getContent().equalsIgnoreCase(prefix + "invite"))
            {
                event.getChannel().sendMessage("You can invite the bot by using the following url: " + api.createBotInvite());
            }
            else if(event.getMessage().getContent().contains(prefix + "cekig"))
            {
                String instaID = event.getMessage().getContent();
                instaID = instaID.replace(";cekig ","");
                System.out.println("Instagram ID : " +instaID);

                //requestHTTP
                String content = null;
                URLConnection connection = null;

                try
                {
                    connection = new URL("https://www.instagram.com/"+instaID+"/").openConnection();
                    Scanner scanner = new Scanner(connection.getInputStream());
                    scanner.useDelimiter("\\Z");
                    content = scanner.next();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                //System.out.println(content);
                if(content == null)
                {
                    event.getChannel().sendMessage("Username Not Found!");
                }

                //get title
                String title = content.substring(content.indexOf("<title>") + 7,content.indexOf("</title>"));
                title = title.replace(" • Instagram photos and videos","");
                title = title.replace("\n","");

                //get userInfo
                //BIO
                String bio = content.substring(content.indexOf("biography\":\""));
                bio = bio.substring(bio.indexOf("\":"));
                bio = bio.replace("\":\"","");
                bio = bio.substring(0,bio.indexOf("\""));
                bio = bio.replace("\\n","\n");

                String externalLinks = "";
                //ExternalLinks
                if(content.indexOf("external_url\":\"") != -1)
                {
                    externalLinks = content.substring(content.indexOf("external_url\":\""));
                    externalLinks = externalLinks.substring(externalLinks.indexOf("\":"));
                    externalLinks = externalLinks.replace("\":\"","");
                    externalLinks = externalLinks.substring(0,externalLinks.indexOf("\""));
                }
                else
                {
                    externalLinks = "No External Links";
                }

                //isPrivateState
                String isPrivate = content.substring(content.indexOf("is_private"));
                isPrivate = isPrivate.substring(isPrivate.indexOf("\":"), isPrivate.indexOf(",\""));
                isPrivate = isPrivate.replace("\":","");

                //getFollower,Following,Posts
                String FFP_Info = content.substring(content.indexOf("<meta content="));
                FFP_Info = FFP_Info.substring(0,FFP_Info.indexOf(" -"));
                FFP_Info = FFP_Info.replace("<meta content=\"","");

                //get ProfilePict URL
                String ppHDURL = content.substring(content.indexOf("\"profile_pic_url_hd\":\""));
                ppHDURL = ppHDURL.substring(22,ppHDURL.indexOf(".jpg")+4);

                System.out.println("INFO:");
                System.out.println(title);
                System.out.println(bio);
                System.out.println(externalLinks);
                System.out.println(isPrivate);
                System.out.println(FFP_Info);

                System.out.println("Profile Picture");
                System.out.println(ppHDURL);

                TextChannel channel = event.getChannel();

                try
                {
                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setTitle("INFO : ")
                            .setImage(ppHDURL)
                            .addField("Name : ", title)
                            .addField("Bio :", bio)
                            .addField("Follower, Following, Post : ",FFP_Info)
                            .addInlineField("isPrivate :" ,isPrivate)
                            .addInlineField("External Links : ",externalLinks)
                            .setFooter(instaID)
                    ;
                    channel.sendMessage(embed);
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                    channel.sendMessage(ex.toString());
                }

            }
        }
        );

        //System.out.println("You can invite the bot by using the following url: " + api.createBotInvite());

    }

}

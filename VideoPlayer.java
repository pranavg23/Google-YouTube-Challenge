package com.google;


import java.util.*;
import java.util.Scanner;

public class VideoPlayer {

  private final VideoLibrary videoLibrary;
  private Boolean playingStatus=false;
  private Boolean pauseStatus=false;
  private String currentVideo="";
  private String currentVideoId="";
  private HashMap<String,List<String>> playlist = new HashMap<>();
  private HashMap <String,String> flag=new HashMap<>();


  public VideoPlayer() {
    this.videoLibrary = new VideoLibrary();
  }

  public void numberOfVideos() {
    System.out.printf("%s videos in the library%n", videoLibrary.getVideos().size());
  }

  public void showAllVideos() {
    System.out.println("Here's a list of all available videos:");
    List<Video> videolist=videoLibrary.getVideos();
    videolist.sort(Comparator.comparing(Video::getTitle));
    for (Video video : videolist) {
      System.out.print("  " + video.getTitle());
      System.out.print(" (" + video.getVideoId() + ")");
      System.out.print(" [" + getTagsString(video)+"]");
      if(flag.containsKey(video.getVideoId()))
      {
        System.out.print(" - FLAGGED ");
        System.out.print("(reason: "+flag.get(video.getVideoId())+")");
      }
      System.out.print("\n");
    }

  }

  private String getTagsString(Video video)
  {
   int size=video.getTags().size();
   String tags="";
   List<String>videoTags=video.getTags();
   int i=0;
   while(i<size)
   {
     if(i==0)
       tags=videoTags.get(i);
     else
       tags=tags +" "+videoTags.get(i);
     i++;
   }
   return tags;
  }

  public void playVideo(String videoId) {
    if(videoLibrary.getVideo(videoId)==null) {
      System.out.println("Cannot play video: Video does not exist");
    }
    else if(flag.containsKey(videoId))
      System.out.println("Cannot play video: Video is currently flagged (reason: "+flag.get(videoId)+")");
    else {
      if (playingStatus) {
        System.out.println("Stopping video: " + currentVideo);
      } else {
        playingStatus = true;
      }
      currentVideoId=videoId;
      currentVideo = videoLibrary.getVideo(videoId).getTitle();
      System.out.println("Playing video: " + currentVideo);
      pauseStatus=false;
    }


  }

  public void stopVideo() {
    if(playingStatus)
    {
      System.out.println("Stopping video: "+currentVideo);
      playingStatus=false;
    }
    else
    {
      System.out.println("Cannot stop video: No video is currently playing");
    }
  }

  public void playRandomVideo() {
    int random= (int) (Math.random() * 4);
    if(videoLibrary.getVideos().size()==0 || videoLibrary.getVideos().size()==flag.keySet().size())
      System.out.println("No videos available");
    else {
      if (playingStatus) {
        System.out.println("Stopping video: " + currentVideo);
      }
      do {
        currentVideo = videoLibrary.getVideos().get(random).getTitle();
        currentVideoId = videoLibrary.getVideos().get(random).getVideoId();
      } while (flag.containsKey(currentVideoId));
      System.out.println("Playing video: " + currentVideo);
      playingStatus = true;
      pauseStatus = false;
    }
  }

  public void pauseVideo() {
    if(playingStatus)
    {
      if(pauseStatus)
      {
        System.out.println("Video already paused: "+currentVideo);
      }
      else
      {
        pauseStatus=true;
        System.out.println("Pausing video: "+currentVideo);
      }
    }
    else
    {
      System.out.println("Cannot pause video: No video is currently playing");
    }

  }

  public void continueVideo() {
    if(playingStatus) {
      if (pauseStatus) {
        pauseStatus = false;
        System.out.println("Continuing video: " + currentVideo);
      } else {
        System.out.println("Cannot continue video: Video is not paused");
      }
    }
    else
    {
      System.out.println("Cannot continue video: No video is currently playing");
    }

  }

  public void showPlaying() {
    if(playingStatus)
    {
        System.out.printf("Currently playing: %s (%s) [%s] ",videoLibrary.getVideo(currentVideoId).getTitle(),videoLibrary.getVideo(currentVideoId).getVideoId(),getTagsString(videoLibrary.getVideo(currentVideoId)));
      if(pauseStatus) {
      System.out.println("- PAUSED");
      }
    }
    else
    {
      System.out.println("No video is currently playing");
    }
  }


  private String checkHashMapKey(String playlistName)
  {
    for(String existingNames: playlist.keySet())
    {
      if(existingNames.equalsIgnoreCase(playlistName))
        return existingNames;
    }
    return null;
  }

  public void createPlaylist(String playlistName) {
    List <String> videoList= new ArrayList<>();
    if(playlist.isEmpty())
    {

      playlist.put(playlistName,videoList);
      System.out.println("Successfully created new playlist: "+playlistName);
    }
    else
    {
      int count=0;
      for(String Names: playlist.keySet())
      {
        if(Names.equalsIgnoreCase(playlistName)) {
          System.out.println("Cannot create playlist: A playlist with the same name already exists");
          break;
        }
        count++;
      }
      if(count==playlist.size())
      {
        playlist.put(playlistName,videoList);
        System.out.println("Successfully created new playlist: "+playlistName);
      }
    }


  }

  public void addVideoToPlaylist(String playlistName, String videoId) {
    String existingPlaylistName=checkHashMapKey(playlistName);
    if(existingPlaylistName==null)
    {
      System.out.println("Cannot add video to "+playlistName+": Playlist does not exist");
    }
    else if(videoLibrary.getVideo(videoId)==null)
    {
      System.out.println("Cannot add video to "+playlistName+": Video does not exist");
    }
    else if(playlist.get(existingPlaylistName).contains(videoId))
    {
      System.out.println("Cannot add video to "+playlistName+": Video already added");
    }
    else if(flag.containsKey(videoId))
    {
      System.out.println("Cannot add video to "+playlistName+": Video is currently flagged (reason: "+flag.get(videoId)+")");
    }
    else
    {
      playlist.get(existingPlaylistName).add(videoId);
      System.out.println("Added video to "+playlistName+": "+videoLibrary.getVideo(videoId).getTitle());
    }

  }

  public void showAllPlaylists() {
    if(playlist.isEmpty())
    {
      System.out.println("No playlists exist yet");
    }
    else
    {
      System.out.println("Showing all playlists:");
      TreeMap<String, List<String>> sorted = new TreeMap<>(playlist);
      for(Map.Entry<String, List<String>> playlistSorted: sorted.entrySet())
      {
        System.out.println("  "+playlistSorted.getKey());
      }
    }
  }

  public void showPlaylist(String playlistName) {
    String existingPlaylistName=checkHashMapKey(playlistName);
    if(existingPlaylistName==null)
    {
      System.out.println("Cannot show playlist "+playlistName+": Playlist does not exist");
    }
    else
    {
      List<String> selectedPlaylist=playlist.get(existingPlaylistName);
      System.out.println("Showing playlist: "+playlistName);
      if(selectedPlaylist.isEmpty()) {
        System.out.println("  No videos here yet");
      }
      else
      {
        for(String videoId: selectedPlaylist)
        {
          Video video=videoLibrary.getVideo(videoId);
          if(flag.containsKey(video.getVideoId())) {
            System.out.print(video.getTitle());
            System.out.print(" (" + video.getVideoId() + ")");
            System.out.print(" [" + getTagsString(video) + "]");
            System.out.print(" - FLAGGED ");
            System.out.print("(reason: " + flag.get(video.getVideoId()) + ")");
          }
          else
          {
            System.out.print("  " + video.getTitle());
            System.out.print(" (" + video.getVideoId() + ")");
            System.out.print(" [" + getTagsString(video) + "]");
          }
          System.out.print("\n");

        }
      }
    }

  }

  public void removeFromPlaylist(String playlistName, String videoId) {
    String existingPlaylistName=checkHashMapKey(playlistName);
    if(existingPlaylistName==null)
    {
      System.out.println("Cannot remove video from "+playlistName+": Playlist does not exist");
    }
    else if(videoLibrary.getVideo(videoId)==null)
    {
      System.out.println("Cannot remove video from "+playlistName+": Video does not exist");
    }
    else if(!playlist.get(existingPlaylistName).contains(videoId))
    {
      System.out.println("Cannot remove video from "+playlistName+": Video is not in playlist");
    }
    else {
      playlist.get(existingPlaylistName).remove(videoId);
      System.out.println("Removed video from "+playlistName+": "+ videoLibrary.getVideo(videoId).getTitle());
    }

  }

  public void clearPlaylist(String playlistName) {
    String existingPlaylistName=checkHashMapKey(playlistName);
    if(existingPlaylistName==null)
    {
      System.out.println("Cannot clear playlist "+playlistName+": Playlist does not exist");
    }
    else
    {
      playlist.get(existingPlaylistName).clear();
      System.out.println("Successfully removed all videos from "+playlistName);
    }
  }

  public void deletePlaylist(String playlistName) {
    String existingPlaylistName=checkHashMapKey(playlistName);
    if(existingPlaylistName==null)
    {
      System.out.println("Cannot delete playlist "+playlistName+": Playlist does not exist");
    }
    else
    {
      playlist.remove(existingPlaylistName);
      System.out.println("Deleted playlist: "+playlistName);
    }

  }
  boolean isNumber(String s)
  {
    for (int i = 0; i < s.length(); i++)
      if (!Character.isDigit(s.charAt(i)))
        return false;

    return true;
  }

  public void searchVideos(String searchTerm) {
    List <Video> searchedTitles=new ArrayList<>();
    String searchTermUpper=searchTerm.toUpperCase();
    int count=0;
    Scanner sc=new Scanner(System.in);
    for(Video video: videoLibrary.getVideos())
    {
      if(video.getTitle().toUpperCase().contains(searchTermUpper))
      {
        if(!flag.containsKey(video.getVideoId())) {
          count++;
          searchedTitles.add(video);
        }
      }
    }
    searchedTitles.sort(Comparator.comparing(Video::getTitle));
    if(count==0)
      System.out.println("No search results for "+searchTerm);
    else
    {
      System.out.println("Here are the results for "+searchTerm+":");
      int i=0;
      while(i<count) {
        System.out.printf(" %d) %s (%s) [%s]%n", (i+1),searchedTitles.get(i).getTitle(),searchedTitles.get(i).getVideoId(),getTagsString(searchedTitles.get(i)));
        i++;
      }
      System.out.println("Would you like to play any of the above? If yes, specify the number of the video.");
      System.out.println("If your answer is not a valid number, we will assume it's a no.");
      String number=sc.next();

      if(isNumber(number)) {
        int no=Integer.parseInt(number);
        if (no <= searchedTitles.size() && no > 0) {
          playVideo(searchedTitles.get((no - 1)).getVideoId());
        }
      }
    }


  }

  public void searchVideosWithTag(String videoTag) {
    List <Video> searchedTitles=new ArrayList<>();
    String searchTermUpper=videoTag.toUpperCase();
    int count=0;
    Scanner sc=new Scanner(System.in);
    for(Video video: videoLibrary.getVideos())
    {
      if(getTagsString(video).toUpperCase().contains(searchTermUpper))
      {
        if(!flag.containsKey(video.getVideoId())) {
          count++;
          searchedTitles.add(video);
        }
      }
    }
    searchedTitles.sort(Comparator.comparing(Video::getTitle));
    if(count==0)
      System.out.println("No search results for "+videoTag);
    else
    {
      System.out.println("Here are the results for "+videoTag+":");
      int i=0;
      while(i<count) {
        System.out.printf(" %d) %s (%s) [%s]%n", (i+1),searchedTitles.get(i).getTitle(),searchedTitles.get(i).getVideoId(),getTagsString(searchedTitles.get(i)));
        i++;
      }
      System.out.println("Would you like to play any of the above? If yes, specify the number of the video.");
      System.out.println("If your answer is not a valid number, we will assume it's a no.");
      String number=sc.next();

      if(isNumber(number)) {
        int no=Integer.parseInt(number);
        if (no <= searchedTitles.size() && no > 0) {
          playVideo(searchedTitles.get((no - 1)).getVideoId());
        }
      }
    }
  }

  public void flagVideo(String videoId) {
    if(flag.containsKey(videoId))
      System.out.println("Cannot flag video: Video is already flagged");
    else if(videoLibrary.getVideo(videoId)==null)
      System.out.println("Cannot flag video: Video does not exist");
    else {
      if(currentVideoId.equals(videoId))
        stopVideo();
      flag.put(videoId, "Not supplied");
      System.out.printf("Successfully flagged video: %s (reason: %s)",videoLibrary.getVideo(videoId).getTitle(),flag.get(videoId));
      System.out.println();
    }

  }

  public void flagVideo(String videoId, String reason) {
    if(flag.containsKey(videoId))
      System.out.println("Cannot flag video: Video is already flagged");
    else if(videoLibrary.getVideo(videoId)==null)
      System.out.println("Cannot flag video: Video does not exist");
    else {
      if(currentVideoId.equals(videoId))
        stopVideo();
      System.out.printf("Successfully flagged video: %s (reason: %s)",videoLibrary.getVideo(videoId).getTitle(),reason);
      System.out.println();
      flag.put(videoId, reason);
    }

  }

  public void allowVideo(String videoId) {
    if(videoLibrary.getVideo(videoId)==null)
      System.out.println("Cannot remove flag from video: Video does not exist");
    else if(!flag.containsKey(videoId))
      System.out.println("Cannot remove flag from video: Video is not flagged");
    else
    {
      flag.remove(videoId);
      System.out.println("Successfully removed flag from video: "+videoLibrary.getVideo(videoId).getTitle());
    }


  }
}
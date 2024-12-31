package com.cooksys.spring_assessment_social_media_nov_2024_team_2.services.HashtagServiceImpl;

import com.cooksys.spring_assessment_social_media_nov_2024_team_2.dtos.HashtagDto;
import com.cooksys.spring_assessment_social_media_nov_2024_team_2.dtos.TweetResponseDto;
import com.cooksys.spring_assessment_social_media_nov_2024_team_2.entities.Hashtag;
import com.cooksys.spring_assessment_social_media_nov_2024_team_2.entities.Tweet;
import com.cooksys.spring_assessment_social_media_nov_2024_team_2.exceptions.NotFoundException;
import com.cooksys.spring_assessment_social_media_nov_2024_team_2.mappers.HashtagMapper;
import com.cooksys.spring_assessment_social_media_nov_2024_team_2.mappers.TweetMapper;
import com.cooksys.spring_assessment_social_media_nov_2024_team_2.repositories.HashtagRepository;
import com.cooksys.spring_assessment_social_media_nov_2024_team_2.services.HashtagService;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class HashtagServiceImpl implements HashtagService {

    private final TweetMapper tweetMapper;
    private HashtagRepository hashtagRepository;
    private HashtagMapper hashtagMapper;

    @Override
    public List<HashtagDto> getAllHashtags() {
        return hashtagMapper.entitiesToDto(hashtagRepository.findAll());
    }

    //  GET tags/{label}
    // Retrieves all (non-deleted) tweets tagged with the given hashtag label in reverse chronological order
    @Override
    public List<TweetResponseDto> getTweetsByHashtag(String label) {

        //  If no hashtag with the given label exists, an error should be sent in lieu of a response.
        Optional<Hashtag> hashtagToFind = hashtagRepository.findByLabel("#" + label);
        if(hashtagToFind.isEmpty()) {
            throw new NotFoundException("Hashtag not found");
        }

        // Changes the Optional back to a Hashtag
        Hashtag hashtag = hashtagToFind.get();

        // Finding all tweets that contain this hashtag
        List<Tweet> tweets = hashtag.getTweets();

        // Finding all non-deleted tweets and adding them to activeTweets
        List<Tweet> activeTweets = new ArrayList<>();
        for (Tweet t : tweets) {
            if (!t.isDeleted()) {
                activeTweets.add(t);
            }
        }
        // Sorting active tweets by reverse chronological order
        activeTweets.sort((t1, t2) -> t2.getPosted().compareTo(t1.getPosted()));

        return tweetMapper.entitiesToDtos(activeTweets);
    }

    }

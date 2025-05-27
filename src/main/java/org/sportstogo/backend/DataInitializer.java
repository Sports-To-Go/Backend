package org.sportstogo.backend;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.sportstogo.backend.Enums.GroupRole;
import org.sportstogo.backend.Enums.MessageType;
import org.sportstogo.backend.Models.*;
import org.sportstogo.backend.Repository.*;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Profile("dev") // Only runs in dev profile
@RequiredArgsConstructor
public class DataInitializer {
    private final List<String> uids = List.of(
            "9VHh0VTHJnYr6gsliLYUjEnKLuW2",
            "ts6iyJ0Jv2NlMFmq3PhktniMCH63",
            "X5AwToiWnHdz25Xd3nkkMLffED93",
            "dM1O1uNJlJdkiUYhDPG8rFPTDgd2",
            "UzDUFmjfm0XyG3k9MrTmyoHZ9w63"
    );

    // Keep these in memory for later reuse
    private final Map<String, User> userMap = new HashMap<>();
    private final List<Group> testGroups = new ArrayList<>();

    private final UserRepository userRepo;
    private final GroupRepository groupRepo;
    private final GroupMembershipRepository membershipRepo;
    private final MessageRepository messageRepo;
    private final JoinRequestRepository joinRequestRepo;

    @PostConstruct
    public void init() {
        populateUsers();
        populateGroups();
        populateGroupMemberships();
        populateMessages();
        populateJoinRequests();
    }

    private void populateUsers() {
        Map<String, String> descriptions = Map.of(
                uids.get(0), "Friendly footballer from Cluj",
                uids.get(1), "Basketball coach and part-time comedian",
                uids.get(2), "Tennis enthusiast who loves dogs",
                uids.get(3), "Rookie ping-pong player with big dreams",
                uids.get(4), "Volleyball champ and hiking nerd"
        );

        uids.forEach(uid -> {
            User user = new User();
            user.setUid(uid);
            user.setDescription(descriptions.get(uid));
            user.setAdmin(false);
            userRepo.save(user);
            userMap.put(uid, user); // store for later
        });
    }

    private void populateGroups() {
        Group group1 = new Group();
        group1.setName("Cluj Football Legends");
        group1.setDescription("Weekly friendly matches and beers 🍻");
        group1.setCreatedBy(userMap.get(uids.getFirst()));
        group1.setCreatedDate(LocalDate.now());
        groupRepo.save(group1);

        Group group2 = new Group();
        group2.setName("Basketball Buzău");
        group2.setDescription("We play. We dunk. We laugh.");
        group2.setCreatedBy(userMap.get(uids.get(1)));
        group2.setCreatedDate(LocalDate.now());
        groupRepo.save(group2);

        testGroups.add(group1);
        testGroups.add(group2);
    }

    private void populateGroupMemberships() {
        Group group1 = testGroups.get(0);
        Group group2 = testGroups.get(1);

        // Add 3 members to group 1
        addMembership(group1, uids.get(0), GroupRole.admin);
        addMembership(group1, uids.get(1), GroupRole.member);
        addMembership(group1, uids.get(2), GroupRole.co_admin);

        // Add 2 members to group 2
        addMembership(group2, uids.get(1), GroupRole.admin);
        addMembership(group2, uids.get(4), GroupRole.member);
    }

    private void addMembership(Group group, String uid, GroupRole role) {
        GroupMembership membership = new GroupMembership();
        membership.setGroupID(group);
        membership.setUserID(userMap.get(uid));
        membership.setGroupRole(role);
        membership.setJoinTime(LocalDateTime.now());
        membershipRepo.save(membership);
    }

    private void populateMessages() {
        for (Group group : testGroups) {
            User author = group.getCreatedBy();

            // 🟡 1. System message: "Group created"
            Message systemMsg = new Message();
            systemMsg.setGroupID(group);
            systemMsg.setUserID(author);
            systemMsg.setType(MessageType.SYSTEM);
            systemMsg.setContent("Group created 🎉");
            systemMsg.setTimeSent(LocalDateTime.now());
            messageRepo.save(systemMsg);

            // 🟡 2. Fetch all members of the group for message rotation
            List<GroupMembership> members = membershipRepo.findAll()
                    .stream()
                    .filter(m -> m.getGroupID().getId().equals(group.getId()))
                    .toList();

            if (members.isEmpty()) continue;

            // 🟡 3. Sample chat lines
            String[] sampleMessages = {
                    "Hello everyone!",
                    "Who's up for a game this weekend?",
                    "I'm bringing the drinks! 🍻",
                    "Let's book the field early.",
                    "Did anyone see last week's match?",
                    "We need to improve our defense!",
                    "Good vibes only 🔥",
                    "How’s everyone doing?",
                    "I'm out this Sunday, sorry!",
                    "Can we start earlier this time?",
                    "Don’t forget the football. Last time was chaos 😂",
                    "We should invite more players.",
                    "Next match: rematch vs. the rivals 😤",
                    "Let’s try a new tactic next game.",
                    "Check out this strategy video: [YouTube Link]",
                    "Anyone injured? Hope not 🤕",
                    "Let's bring uniforms next time.",
                    "GG everyone, amazing effort!",
                    "I'll handle the booking this week.",
                    "Bring some cones for drills!"
            };

            // 🟡 4. Create 60+ text messages rotating authors
            LocalDateTime baseTime = LocalDateTime.now().plusMinutes(2);
            int count = 0;

            for (int i = 0; i < 60; i++) {
                GroupMembership senderMembership = members.get(i % members.size());
                User sender = senderMembership.getUserID();

                Message msg = new Message();
                msg.setGroupID(group);
                msg.setUserID(sender);
                msg.setType(MessageType.TEXT);
                msg.setContent(sampleMessages[i % sampleMessages.length]);
                msg.setTimeSent(baseTime.plusSeconds(i * 60L)); // simulate 1 min gaps
                messageRepo.save(msg);

                count++;
            }

            System.out.println("Injected " + count + " messages in group: " + group.getName());
        }
    }


    private void populateJoinRequests() {
        Group group1 = testGroups.getFirst();
        User requestingUser = userMap.get(uids.get(3)); // 4th user

        JoinRequest req = new JoinRequest();
        req.setGroupID(group1);
        req.setUserID(requestingUser);
        req.setMotivation("Hey! I love football, would love to join you.");
        req.setRequestTime(LocalDateTime.now());
        joinRequestRepo.save(req);
    }
}

package com.example.swithme.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "groupUsers")
public class GroupUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "groupId", nullable = false)
    private Group group;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    public GroupUser(User user, Group group) {
        this.user = user;
        this.group = group;
    }
}

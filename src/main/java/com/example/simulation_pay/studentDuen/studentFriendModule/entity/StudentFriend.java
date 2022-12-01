package com.example.simulation_pay.studentDuen.studentFriendModule.entity;

    import java.io.Serializable;
    import lombok.Data;
    import lombok.EqualsAndHashCode;
    import lombok.experimental.Accessors;

/**
* <p>
    * 学生好友表
    * </p>
*
* @author 龚某鑫
* @since 2021-07-02
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    public class StudentFriend implements Serializable {

    private static final long serialVersionUID = 1L;

            /**
            * 用户ID
            */
    private String userUuid;

            /**
            * 好友ID
            */
    private String friendId;

            /**
            * 是否好友 1是 0否
            */
    private String isFriend;

            /**
            * 删除标记
            */
    private Integer delFlag;


}

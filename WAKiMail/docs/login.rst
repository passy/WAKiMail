=====
Login
=====

Consideration
=============

    * Order
        #. Get Challenge
        #. Login with Challenge

    * LoginTask calls LoginManager
        * Get Challenge
        * Update Progress Dialog
        * Sent Credentials
        * Verify correct response
            * Throw exception if invalid
            * Return User object if valid (how?)

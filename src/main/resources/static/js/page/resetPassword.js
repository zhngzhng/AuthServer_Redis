new Vue({
    el: "#app",
    data: function (){
        var validatePass = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('Please enter password'));
            }
            //????为什么添加此步即可
            else {
                if (this.resetForm.checkPass !== '') {
                    this.$refs.resetForm.validateField('checkPass');
                }
                callback();
            }
        };
        var validatePass2 = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('Please enter password again'));
            } else if (value !== this.resetForm.newPwd) {
                callback(new Error('Password and Confirm Password are inconsistent!'));
            } else {
                callback();
            }
        };
        return{
            resetForm: {
                email: '',
                oldPwd: '',
                newPwd: '',
                checkPass: ''
            },
            rules1: {
                newPwd: [
                    {validator: validatePass, trigger: 'blur'}
                ],
                chekPass: [
                    {validator: validatePass2, trigger: 'blur'}
                ]
            },
        }
    },
    methods: {
        submitForm(formName){
            this.$refs[formName].validate((valid)=>{
                if (valid){
                    this.updatePassword()
                }else {
                    this.$notify.error({
                        title: 'Error',
                        message: 'Please check your information.',
                        offset: 70
                    });
                    return false;
                }
            })
        },
        updatePassword(){
            $.ajax({
                url: '/AuthServer/user/newPassword',
                type: 'post',
                data: this.resetForm,
                success: (result)=>{
                    if (result === 1){
                        this.$notify.success({
                            title: 'Success',
                            message: 'Reset successful ! Redirecting.....',
                            offset: 70
                        })
                        setTimeout(()=>{
                            window.location.href = "/AuthServer/login"
                        },500)
                    }else {
                        this.$notify.error({
                            title: 'Error',
                            message: 'Please check the old password.'
                        })
                    }
                },
                error:function (e){
                    this.$message({
                        type: 'Error',
                        message: 'Reset failed,email or password is wrong!',
                        offset: 40,
                        showClose: true,
                    })
                }
            })
        }

    },
    mounted(){
        this.resetForm.email =  window.sessionStorage.getItem("email");
    }
})
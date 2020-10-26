new Vue({
    el: '#app',
    data(){
        var validateEmail = (rule, value, callback) => {
            let reg = /^[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
            let flag = value.match(reg);
            let reg1 = /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
            if(flag == null){
                flag = value.match(reg1);
            }
            if (value === '') {
                callback(new Error('Please enter email address'));
            } else if (flag==null){
                callback(new Error('Please enter the correct email address'));
            }else {
                callback();
            }
        };
        var validateName = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('Please enter your name'));
            } else {
                callback();
            }
        };
        var validateOrg = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('Please enter your affiliation'));
            } else {
                callback();
            }
        };
        var validateCountry = (rule, value, callback)=>{
            if (value === ''){
                callback(new Error('Please enter your country or region'))
            }else {
                callback();
            }
        };
        var validateTitle = (rule, value, callback)=>{
            if (value === ''){
                callback(new Error('Please select your title'))
            }else {
                callback();
            }
        };
        var validatePass = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('Please enter password'));
            } else {
                if (this.ruleForm2.checkPass !== '') {
                    this.$refs.ruleForm2.validateField('checkPass');
                }
                callback();
            }
        };
        var validatePass2 = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('Please enter password again'));
            } else if (value !== this.ruleForm2.password) {
                callback(new Error('Password and Confirm Password are inconsistent!!'));
            } else {
                callback();
            }
        };
        return {
            //navbar
            activeIndex: '7',
            //register form
            ruleForm2: {
                email:'',
                password: '',
                name:'',
                title: '',
                country: '',
                organizations: null,
                checkPass: '',
            },
            //自定义验证规则绑定
            rules2: {
                email:[
                    { validator: validateEmail, trigger: 'blur' }
                ],
                password: [
                    { validator: validatePass, trigger: 'blur' }
                ],
                checkPass: [
                    { validator: validatePass2, trigger: 'blur' }
                ],
                name: [
                    { validator: validateName, trigger: 'blur' }
                ],
                organizations: [
                    { validator: validateOrg, trigger: 'blur' }
                ],
                country: [
                    {validator: validateCountry, trigger: 'blur'}
                ],
                title: [
                    {validator: validateTitle, trigger: 'blur'}
                ]
            },
            //org tag
            //dynamicTags: [],
            inputVisible: false,
            inputValue: ''
        }
    },
    methods: {
        //register form
        submitForm(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    this.ruleForm2.organizations = this.ruleForm2.organizations.toString().split(",");
                    this.register();
                } else {
                    this.$notify.error({
                        title: 'Error',
                        message: 'Please check your registration information.',
                        offset: 70
                    });
                    return false;
                }
            });
        },
        resetForm(formName){
            this.$refs[formName].resetFields();
        },
        register(){
            $.ajax({
                url : '/AuthServer/user/add',
                type : 'post',
                // data对象中的属性名要和服务端控制器的参数名一致 login(name, password)
                data : this.ruleForm2,
                //将json form-data转换为json
                // dataType:"json",
                contentType:"application/json",
                data: JSON.stringify(this.ruleForm2),
                success : (result)=> {
                    if(result === 1) {
                        this.$notify.success({
                            title: 'Success',
                            message: 'Register successful!',
                            offset: 70
                        });
                        setTimeout(()=>{
                            window.location.href = '/AuthServer/login'
                        },2000)
                    }
                    else if(result === -1){
                        this.$notify.error({
                            title: 'Error',
                            message: 'Email has existed, please change your Email',
                            offset: 70
                        });
                    }
                    else if(result ===-2){
                        this.$notify.error({
                            title: 'Error',
                            message: 'Email has existed, please change your Email',
                            offset: 70
                        });
                    }
                },
                error : (e)=> {
                    this.$message({
                        showClose: true,
                        message: 'register error',
                        type: 'error'
                    });
                }
            });
        }
    }
})
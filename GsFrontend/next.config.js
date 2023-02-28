/** @type {import('next').NextConfig} */
const nextConfig = {
    reactStrictMode: false,
    trailingSlash: true,
    basePath: '',
    publicRuntimeConfig: {
        contextPath:'',
        uploadPath: ''
    },
    env: {
        API_URL: process.env.API_URL
    }
};

module.exports = nextConfig;

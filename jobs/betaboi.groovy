String app = "WebStatic"

folder("${app}") {
  description "ini adalah description"
}

job("${app}/Beta") {
    description "web boi utama"
    logRotator {
        daysToKeep(2)
        numToKeep(2)
    }
    scm {
        git {
            remote {
                url('https://github.com/BlankOn/blankon-linux-static-web.git')
            }
            branch('master')
        }
    }
    triggers {
        scm('H/5 * * * *')
    } 

    steps {
     	shell('''node -v && npm -v
npm i
npm run build
tar -czvf dist.tar.gz dist
rsync -avz --rsh="ssh -p2222" --progress dist/ situs@waljinah.blankon.in:/home/situs/beta/dist/
echo "sukses"''')   
    }
    publishers{
        archiveArtifacts {
            pattern("dist.tar.gz")
            onlyIfSuccessful()
        }
    }
}

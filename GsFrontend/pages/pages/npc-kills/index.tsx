import { Column } from 'primereact/column';
import BackendService from '../../../demo/service/BackendService';
import TopResultTable from '../../../demo/components/TopResultTable';

const NpcKillPage = () => {

    return (
        <TopResultTable 
            title="Top Npc Kills" 
            getGlobalRanking={() => BackendService.getTopNpcKills()}
            columns={
                [<Column field="playerName" header="PlayerName"/>,
                <Column field="PGS_NPC_KILLS" header="Npc Kills"/>]}/> 
    );
};

export default NpcKillPage;